package org.be.airqualitymonitoring.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.be.airqualitymonitoring.AqiApplication;
import org.be.airqualitymonitoring.dao.BreakpointAqiDao;
import org.be.airqualitymonitoring.dao.MeasurementDao;
import org.be.airqualitymonitoring.entity.Measurement;
import org.be.airqualitymonitoring.entity.Sensor;
import org.be.airqualitymonitoring.error.HourlyOzoneLowLevelException;
import org.be.airqualitymonitoring.error.InsufficientDataForAqiCalculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MeasurementServiceImpl implements MeasurementService {

	private static final Logger LOGGER = LogManager.getLogger(AqiApplication.class);

	private MeasurementDao measurementDao;
	private BreakpointAqiDao breakpointAqiDao;
	private SensorService sensorService;

	@Autowired
	public MeasurementServiceImpl(MeasurementDao measurementDao, BreakpointAqiDao breakpointAqiDao,SensorService sensorService) {
		this.measurementDao = measurementDao;
		this.breakpointAqiDao = breakpointAqiDao;
		this.sensorService=sensorService;
	}

	
	@Override
	public boolean isDataValid(int sensorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		List<String> pollutants = new ArrayList<>(
				Arrays.asList("ozone", "pm10", "pm25", "sulfurDioxide", "carbonMonoxide", "nitrogenDioxide"));
		int count = 0;
		boolean pmHasEnoughData = false;

		for (String pollutant : pollutants) {
			try {
				boolean hasEnoughData = isSufficientDataAvailable(sensorId, pollutant, startDateTime, endDateTime);
				if (hasEnoughData) {
					count++;
					if (pollutant.equals("pm10") || pollutant.equals("pm25")) {
						pmHasEnoughData = true;
					}
				}
			} catch (DataAccessException e) {
				throw e;
			}
		}

		if (count >= 3 && pmHasEnoughData) {
			return true;
		}
		return false;
	}

	public double dailyAverageOzone(int sensorId, LocalDate date) throws InsufficientDataForAqiCalculation {
		String pollutantName = "ozone";
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(23, 0);
		int windowSize = 8;
		int countValid8HrAverage = 0;
		Double maximumAverage = 0.0;
		Double movingAverage = 0.0;
		String format = null;
		DecimalFormat df = truncateDecimal(pollutantName);

		// check if a pollutant has a 75% data coverage per 24 hrs.
		if (isSufficientDataAvailable(sensorId, pollutantName, startOfDay, endOfDay)) {
			List<Double> ozoneData = measurementDao.getDailyOzone(sensorId, startOfDay, endOfDay);
			int minimumRequiredHours = 6;
			for (int i = 0; i + windowSize <= ozoneData.size(); i++) {

				// create subList(i,i+averageSize) of 8 hr window
				List<Double> eightHrData = ozoneData.subList(i, i + windowSize);
				// count non null values within a 8 hr window
				long nonNullValues = eightHrData.stream().filter(data -> Objects.nonNull(data)).count();
				// check if number of hours with valid(non-null) data are at least 6 hrs
				// (minimum required hrs) in a 8 hr period
				if (nonNullValues >= minimumRequiredHours) {

					movingAverage = eightHrData.stream().filter(data -> Objects.nonNull(data))
							.collect(Collectors.averagingDouble(Double::doubleValue));// truncated to .001ppm/1ppb
					countValid8HrAverage++;
				}

			}

			if (maximumAverage < movingAverage) {
				maximumAverage = movingAverage;
			}
			// check if 75% or 13/17 of the 8-hr averages are present
			if (countValid8HrAverage < 13) {
				throw new InsufficientDataForAqiCalculation();
			}

		}

		else {
			throw new InsufficientDataForAqiCalculation();
		}

		format = df.format(maximumAverage);
		return Double.valueOf(format);
	}

	/*
	 * https://forum.airnowtech.org/t/daily-and-hourly-aqi-ozone/170 Hourly Ozone
	 * AQI If viewing/calculating hourly ozone AQI for hours >1 old, these
	 * calculations are done using the midpoint 8-hour average ozone concentration
	 * which is then converted to AQI. For a given hour, this includes the previous
	 * 4 hours, the given hour, and the following 3 hours. 6 of 8 hourly
	 * concentrations (75%) are needed for a valid midpoint 8-hour average.
	 */
	@Override
	public Double hourlyOzone(int sensorId, LocalDateTime dateTime)
			throws InsufficientDataForAqiCalculation, HourlyOzoneLowLevelException, DataAccessException {
		// if current hr reading, return sensor reading directly
		String pollutantName = "ozone";
		LocalDateTime currentDateTime = LocalDateTime.now();
		Duration duration = Duration.between(dateTime, currentDateTime);
		int minimumRequiredHrs = 6;
		Double ozoneReading = 0.0;
		String format = null;
		DecimalFormat df = truncateDecimal(pollutantName);
		if (duration.toHours() < 1) {
			ozoneReading = measurementDao.getHourlyOzone(sensorId, dateTime);

		} else {
			LocalDateTime minimumWindowHr = dateTime.minusHours(4);
			LocalDateTime maximumWindowHr = dateTime.plusHours(3);

			List<Double> ozoneData = measurementDao.getDailyOzone(sensorId, minimumWindowHr, maximumWindowHr);
			long noNonNullValues = ozoneData.stream().filter(data -> Objects.nonNull(data)).count();
			if (noNonNullValues < minimumRequiredHrs) {
				throw new InsufficientDataForAqiCalculation();
			} else {
				ozoneReading = ozoneData.stream().filter(data -> Objects.nonNull(data))
						.collect(Collectors.averagingDouble(Double::doubleValue));
			}
		}
				
		// if reading <0.125 ppm...use the calculateDailyAverageOzone()
		if (ozoneReading < 1.25E-7) {
			throw new HourlyOzoneLowLevelException(
					"Hourly ozone reading is below 0.125 ppm. Calculate the daily average instead.");
		}
		format = df.format(ozoneReading);
		return Double.valueOf(format);
	}

	/*
	 * https://forum.airnowtech.org/t/daily-and-hourly-aqi-pm2-5/171 The daily PM2.5
	 * AQI is calculated by taking the 24-hour concentration average from midnight
	 * to midnight (Local Standard Time) and converting to AQI using the PM2.5
	 * cutpoints. 75%, or 18/24 hours of data are needed for a valid daily AQI
	 * calculation.
	 */
	@Override
	public double dailyAveragePM(int sensorId, String pollutantName, LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(23, 0);
		String format = null;
		DecimalFormat df = truncateDecimal(pollutantName);
		List<Double> pmData = null;
		if (pollutantName.equals("pm10")) {
			pmData = measurementDao.getDailyPm10(sensorId, startOfDay, endOfDay);

		}
		if (pollutantName.equals("pm25")) {
			pmData = measurementDao.getDailyPm25(sensorId, startOfDay, endOfDay);

		}
		double dailyAverage = 0;
		if (isSufficientDataAvailable(sensorId, pollutantName, startOfDay, endOfDay)) {
			dailyAverage = pmData.stream().filter(data -> Objects.nonNull(data))
					.collect(Collectors.averagingDouble(Double::doubleValue));
		}
		format = df.format(dailyAverage);

		return Double.valueOf(format);
	}

	/*
	 * If viewing/calculating hourly PM10 AQI for times >=6 hours old, the hourly
	 * AQI calculations should be done by using the midpoint 24-hour average
	 * concentration converted to AQI. For a given hour, this includes the previous
	 * 12 hours, the given hour, and the following 11 hours. 18 of 24 hourly
	 * concentrations (75%) are needed for a midpoint 24-hour average. 24-hour
	 * average mdpoint concentrations are truncated to the nearest 1 ug/m3
	 * 
	 * For real-time readings and readings less than 6 hours old, the NowCast is
	 * used to calculate the AQI for each hour.
	 * https://forum.airnowtech.org/t/the-nowcast-for-pm2-5-and-pm10/172 1. Compute
	 * the concentrations range (max-min) over the last 12 hours. 2. Divide the
	 * range by the maximum concentration in the 12 hour period to obtain the scaled
	 * rate of change. 3. Compute the weight factor by subtracting the scaled rate
	 * from 1. The weight factor must be between .5 and 1. The minimum limit
	 * approximates a 3-hour average. If the weight factor is less than .5, then set
	 * it equal to .5. 4. Multiply each hourly concentration by the weight factor
	 * raised to the power of how many hours ago the concentration was measured (for
	 * the current hour, the factor is raised to the zero power). 5. Compute the
	 * NowCast by summing these products and dividing by the sum of the weight
	 * factors raised to the power of how many hours ago the concentration was
	 * measured.
	 */
	 

	@Override
	public double hourlyPM(int sensorId, String pollutantName, LocalDateTime dateTime)
			throws InsufficientDataForAqiCalculation, NoSuchElementException {
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime minimumWindowTime = dateTime.minusHours(12);
		LocalDateTime maximumWindowTime = dateTime.plusHours(11);
		List<Double> past12HoursMeasurements = null;
		if (pollutantName.equals("pm10")) {
			past12HoursMeasurements = measurementDao.getDailyPm10(sensorId, minimumWindowTime, dateTime);
		}

		if (pollutantName.equals("pm25")) {
			past12HoursMeasurements = measurementDao.getDailyPm25(sensorId, minimumWindowTime, dateTime);

		}
		double max12HrReading = past12HoursMeasurements.stream().mapToDouble(d -> d).max()
				.orElseThrow(NoSuchElementException::new);
		double min12HrReading = past12HoursMeasurements.stream().mapToDouble(d -> d).max()
				.orElseThrow(NoSuchElementException::new);
		double range;
		double rateOfChange = 0;
		double sum = 0;
		double hourlyAverage = 0;
		double weightFactor = 0;
		String format = null;
		DecimalFormat df = truncateDecimal(pollutantName);

		List<Double> pmData = null;
		int elapsedHrs = (int) ChronoUnit.HOURS.between(dateTime, currentDateTime);

		try {
			if (isSufficientDataAvailable(sensorId, pollutantName, minimumWindowTime, maximumWindowTime)) {
				// for time >=6 hrs old
				if (elapsedHrs >= 6) {

					if (pollutantName.equals("pm10")) {
						pmData = measurementDao.getDailyPm10(sensorId, minimumWindowTime, dateTime);
					}

					if (pollutantName.equals("pm25")) {
						pmData = measurementDao.getDailyPm25(sensorId, minimumWindowTime, dateTime);

					}

					hourlyAverage = pmData.stream().filter(data -> Objects.nonNull(data))
							.collect(Collectors.averagingDouble(Double::doubleValue));
				}

				else { // for under 6hrs
					range = max12HrReading - min12HrReading;
					rateOfChange = 1 - (range / max12HrReading);
					if (rateOfChange < 0.5) {
						rateOfChange = 0.5;
					}
					minimumWindowTime = currentDateTime;
					maximumWindowTime = currentDateTime.minusHours(11);

					if (pollutantName.equals("pm10")) {
						pmData = measurementDao.getDailyPm10(sensorId, minimumWindowTime, dateTime);
					}

					if (pollutantName.equals("pm25")) {
						pmData = measurementDao.getDailyPm25(sensorId, minimumWindowTime, dateTime);
					}

					// check if 2 of the most recent 3hrs are valid(not null)
					long noNullData = pmData.subList(1, 4).stream().filter(data -> Objects.isNull(data)).count();

					if (noNullData > 1) {
						throw new InsufficientDataForAqiCalculation(
								"2 of the past 3 hourly data points must be valid.");
					}
					for (int i = 0; i < pmData.size(); i++) {
						if (Objects.nonNull(pmData.get(i))) {
							sum += pmData.get(i) * (Math.pow(rateOfChange, i));
							weightFactor += Math.pow(rateOfChange, i);
						}

					}

					hourlyAverage = sum / weightFactor;
				}
			}

			else {
				throw new InsufficientDataForAqiCalculation();

			}
		} catch (DataAccessException e) {
			throw e;
		} catch (InsufficientDataForAqiCalculation e) {
			throw new InsufficientDataForAqiCalculation();
		}
		format = df.format(hourlyAverage);

		return Double.valueOf(format);
	}

	/*
	 * To calculate AQI, data for a minimum of three pollutants must be present, of
	 * which one should be either PM10 or PM2. 5. Else, data are considered
	 * insufficient for calculating AQI. Similarly, a minimum of 18 hours’ data is
	 * considered necessary for calculating subindex.
	 */
	@Override
	public int calculateAQI(int sensorId, double concentration, double higherBreakpoint, double lowerBreakpoint,
			int AQIHigherBreakpoint, int AQILowerBreakpoint) {

		int Index = (int) (((AQIHigherBreakpoint - AQILowerBreakpoint) / (higherBreakpoint - lowerBreakpoint))
				* (concentration - lowerBreakpoint)) + AQILowerBreakpoint;

		return Index;
	}

	/*
	 * https://www.airnow.gov/sites/default/files/2020-05/aqi-technical-assistance-
	 * document-sept2018.pdf (page#16) EPA
	 * strengthened the primary standard for SO2 in 2010. Because there was not
	 * enough health information to inform changing the upper end of the AQI for
	 * SO2, the upper end continues to use the 24-hour average SO2 concentration.
	 * The lower end of the AQI uses the daily max 1-hour SO2 concentration. If you
	 * have a daily max 1-hour SO2 concentration below 305 ppb, then use the
	 * breakpoints in Table 5 to calculate the AQI value. If you have a 24-hour
	 * average SO2 concentration greater than or equal to 305 ppb, then use the
	 * breakpoints in Table 5 to calculate the AQI value. If you have a 24-hour
	 * value in this range, it will always result in a higher AQI value than a
	 * 1-hour value would. On rare occasions, you could have a day where the daily
	 * max 1-hour concentration is at or above 305 ppb but when you try to use the
	 * 24-hour average to calculate the AQI value, you find that the 24-hour
	 * concentration is not above 305 ppb. If this happens, use 200 for the lower
	 * and upper AQI breakpoints (ILo and IHi) in Equation 1 to calculate the AQI
	 * value based on the daily max 1-hour value. This effectively fixes the AQI
	 * value at 200 exactly, which ensures that you get the highest possible AQI
	 * value associated with your 1-hour concentration on such days.
	 */
	@Override
	public double dailyAverageSulfurDioxide(int sensorId, LocalDate date) {
		String pollutantName = "sulfurDioxide";
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(23, 0);

		List<Double> so2Data = null;
		try {
			so2Data = measurementDao.getDailySulfurDioxide(sensorId, startOfDay, endOfDay);
		} catch (DataAccessException e) {

			e.printStackTrace();
		}
		double dailyAverage = 0;
		String format = null;
		try {
			if (isSufficientDataAvailable(sensorId, pollutantName, startOfDay, endOfDay)) {
				dailyAverage = so2Data.stream().filter(data -> Objects.nonNull(data))
						.collect(Collectors.averagingDouble(Double::doubleValue));
				DecimalFormat df = truncateDecimal(pollutantName);
				format = df.format(dailyAverage);
			}
		} catch (DataAccessException e) {
			throw e;
		}

		return Double.valueOf(format);

	}

	// max1 hr
	@Override
	public double maxHourlySulfurDioxide(int sensorId, LocalDate date) {
		String pollutantName = "sulfurDioxide";
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(23, 0);
		String format = null;
		List<Double> so2Data = null;
		try {
			so2Data = measurementDao.getDailySulfurDioxide(sensorId, startOfDay, endOfDay);
		} catch (DataAccessException e) {
			throw e;
		}
		double maxReading = 0;
		if (isSufficientDataAvailable(sensorId, pollutantName, startOfDay, endOfDay)) {
			maxReading = so2Data.stream().filter(data -> Objects.nonNull(data))
					.max(Comparator.comparing(Double::valueOf)).get();
			DecimalFormat df = truncateDecimal(pollutantName);
			format = df.format(maxReading);
		}

		return Double.valueOf(format);
	}

	// 8 hr avg (moving average value of the last 8 hr)
	@Override
	public double dailyAverageCarbonMonoxide(int sensorId, LocalDate date) throws InsufficientDataForAqiCalculation {
		String pollutantName = "carbonMonoxide";
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(23, 0);
		int windowSize = 8;
		int countValid8HrAverage = 0;
		Double maximumAverage = 0.0;
		Double movingAverage = 0.0;
		String format = null;
		DecimalFormat df = truncateDecimal(pollutantName);

		// check if a pollutant has a 75% data coverage per 24 hrs.
		try {
			if (isSufficientDataAvailable(sensorId, pollutantName, startOfDay, endOfDay)) {
				List<Double> coData = measurementDao.getDailyCarbonMonoxide(sensorId, startOfDay, endOfDay);
				int minimumRequiredHours = 6;
				for (int i = 0; i + windowSize <= coData.size(); i++) {

					// create subList(i,i+averageSize) of 8 hr window
					List<Double> eightHrData = coData.subList(i, i + windowSize);
					// count non null values within a 8 hr window
					long nonNullValues = eightHrData.stream().filter(data -> Objects.nonNull(data)).count();
					// check if number of hours with valid(non-null) data are at least 6 hrs
					// (minimum required hrs) in a 8 hr period
					if (nonNullValues >= minimumRequiredHours) {

						movingAverage = eightHrData.stream().filter(data -> Objects.nonNull(data))
								.collect(Collectors.averagingDouble(Double::doubleValue));// truncated to .001ppm/1ppb
						countValid8HrAverage++;
					}

				}

				if (maximumAverage < movingAverage) {
					maximumAverage = movingAverage;
				}
				// check if 75% or 13/17 of the 8-hr averages are present
				if (countValid8HrAverage < 13) {
					throw new InsufficientDataForAqiCalculation();
				}

			}

			else {
				throw new InsufficientDataForAqiCalculation();
			}
		} catch (DataAccessException e) {
			throw e;
		} catch (InsufficientDataForAqiCalculation e) {
			throw new InsufficientDataForAqiCalculation();
		}

		format = df.format(maximumAverage);

		return Double.valueOf(format);
	}

	// 1 hr avg (real-time value) /max of the 1-hr averages
	@Override
	public double maxHourlyNitrogenDioxide(int sensorId, LocalDate date) {
		String pollutantName = "nitrogenDioxide";
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(23, 0);
		String format = null;
		List<Double> no2Data = null;
		try {
			no2Data = measurementDao.getDailyNitrogenDioxide(sensorId, startOfDay, endOfDay);
		} catch (DataAccessException e) {
			throw e;

		}
		double maxReading = 0;
		try {
			if (isSufficientDataAvailable(sensorId, pollutantName, startOfDay, endOfDay)) {
				maxReading = no2Data.stream().filter(data -> Objects.nonNull(data))
						.max(Comparator.comparing(Double::valueOf)).get();
				DecimalFormat df = truncateDecimal(pollutantName);
				format = df.format(maxReading);

			}
		} catch (DataAccessException e) {
			throw e;
		}

		return Double.valueOf(format);

	}

	@Override
	public Measurement saveMeasurement(Measurement measurement) {
		try {
			return measurementDao.saveMeasurement(measurement);
		} catch (DataAccessException e) {
			throw e;
		}

	}

	@Override
	public Measurement updateMeasurement(int id, Measurement measurement) {
		try {
			return measurementDao.updateMeasurement(id, measurement);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Measurement deleteMeasurement(int id) {
		try {
			return measurementDao.deleteMeasurement(id);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Measurement getMeasurement(int id) {
		try {
			return measurementDao.getMeasurement(id);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	public boolean isSufficientDataAvailable(int sensorId, String pollutant, LocalDateTime startDateTime,
			LocalDateTime endDateTime) {
		List<Double> dailyReading = new ArrayList<>();
		switch (pollutant) {
		case "ozone":
			dailyReading = measurementDao.getDailyOzone(sensorId, startDateTime, endDateTime);
		case "pm10":
			dailyReading = measurementDao.getDailyPm10(sensorId, startDateTime, endDateTime);
		case "pm25":
			dailyReading = measurementDao.getDailyPm25(sensorId, startDateTime, endDateTime);
		case "sulfurDioxide":
			dailyReading = measurementDao.getDailySulfurDioxide(sensorId, startDateTime, endDateTime);
		case "carbonMonoxide":
			dailyReading = measurementDao.getDailyCarbonMonoxide(sensorId, startDateTime, endDateTime);
		case "nitrogenDioxide":
			dailyReading = measurementDao.getDailyNitrogenDioxide(sensorId, startDateTime, endDateTime);

		}
		long samplingCount = dailyReading.stream().filter(p -> Objects.nonNull(p)).count();
		// check if the sample covers 18 or more hours of a 24 hr day period
		if (samplingCount >= 18) {
			return true;
		}
		return false;
	}

	@Override
	public List<Measurement> getMeasurements(int sensorId, LocalDateTime start, LocalDateTime end) {
		try {
			return measurementDao.getMeasurements(sensorId, start, end);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	// truncate decimal results to the nearest decimal places or integer
	public static DecimalFormat truncateDecimal(String pollutantName) {
		DecimalFormat format = null;
		if (pollutantName.equals("ozone") || pollutantName.equals("sulfurDioxide")
				|| pollutantName.equals("nitrogenDioxide")) {
			// truncate to 3 decimal places
			format = new DecimalFormat("#.###");
		} else if (pollutantName.equals("pm25") || pollutantName.equals("carbonMonoxide")) {
			// truncate to 1 decimal place
			format = new DecimalFormat("#.#");

		} else if (pollutantName.equals("pm10")) {
			// truncate to integer
			format = new DecimalFormat("#");

		}

		format.setRoundingMode(RoundingMode.HALF_UP);

		return format;
	}

	// computes hourly individual AQI values for all the pollutants
	@Override
	public int calculateHourlyAQI(int sensorId, String pollutant, LocalDateTime dateTime) throws NoSuchElementException,
			InsufficientDataForAqiCalculation, DataAccessException, HourlyOzoneLowLevelException {
		LocalDateTime startOfDay = dateTime.toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = dateTime.toLocalDate().atTime(23, 0);
		int aqi = 0;
		if (isDataValid(sensorId, startOfDay, endOfDay)) {
			int higherAQIBreakpoint = 0;
			int lowerAQIBreakpoint = 0;
			double higherBreakpoint = 0;
			double lowerBreakpoint = 0;
			switch (pollutant) {
			case "pm10":
				double pm10Concentration = hourlyPM(sensorId, "pm10", dateTime);
				String pm10Max = "pm10Max";
				String pm10Min = "pm10Min";
				higherAQIBreakpoint = breakpointAqiDao.getHigherAQIBreakpoint(pm10Min, pm10Max, pm10Concentration);
				lowerAQIBreakpoint = breakpointAqiDao.getLowerAQIBreakpoint(pm10Min, pm10Max, pm10Concentration);
				higherBreakpoint = breakpointAqiDao.getHigherBreakpoint(pm10Min, pm10Max, pm10Concentration);
				lowerBreakpoint = breakpointAqiDao.getLowerBreakpoint(pm10Min, pm10Max, pm10Concentration);
				aqi = calculateAQI(sensorId, pm10Concentration, higherBreakpoint, lowerBreakpoint, higherAQIBreakpoint,
						lowerAQIBreakpoint);
				return aqi;
			case "pm25":
				double pm25Concentration = hourlyPM(sensorId, "pm25", dateTime);
				String pm25Max = "pm25Max";
				String pm25Min = "pm25Min";
				higherAQIBreakpoint = breakpointAqiDao.getHigherAQIBreakpoint(pm25Min, pm25Max, pm25Concentration);
				lowerAQIBreakpoint = breakpointAqiDao.getLowerAQIBreakpoint(pm25Min, pm25Max, pm25Concentration);
				higherBreakpoint = breakpointAqiDao.getHigherBreakpoint(pm25Min, pm25Max, pm25Concentration);
				lowerBreakpoint = breakpointAqiDao.getLowerBreakpoint(pm25Min, pm25Max, pm25Concentration);
				aqi = calculateAQI(sensorId, pm25Concentration, higherBreakpoint, lowerBreakpoint, higherAQIBreakpoint,
						lowerAQIBreakpoint);
				return aqi;

			case "sulfurDioxide":
				double sulfurDioxideConcentration = maxHourlySulfurDioxide(sensorId, dateTime.toLocalDate());
				String sulfurDioxideMax = "sulfurDioxideMax";
				String sulfurDioxideMin = "sulfurDioxideMin";

				higherAQIBreakpoint = breakpointAqiDao.getHigherAQIBreakpoint(sulfurDioxideMin, sulfurDioxideMax,
						sulfurDioxideConcentration);
				lowerAQIBreakpoint = breakpointAqiDao.getLowerAQIBreakpoint(sulfurDioxideMin, sulfurDioxideMax,
						sulfurDioxideConcentration);
				higherBreakpoint = breakpointAqiDao.getHigherBreakpoint(sulfurDioxideMin, sulfurDioxideMax,
						sulfurDioxideConcentration);
				lowerBreakpoint = breakpointAqiDao.getLowerBreakpoint(sulfurDioxideMin, sulfurDioxideMax,
						sulfurDioxideConcentration);
				aqi = calculateAQI(sensorId, sulfurDioxideConcentration, higherBreakpoint, lowerBreakpoint,
						higherAQIBreakpoint, lowerAQIBreakpoint);
				return aqi;

			case "ozone":
				double ozoneConcentration = hourlyOzone(sensorId, dateTime);
				String ozone1hrMax = "ozone1hrMax";
				String ozone1hrMin = "ozone1hrMin";

				higherAQIBreakpoint = breakpointAqiDao.getHigherAQIBreakpoint(ozone1hrMin, ozone1hrMax,
						ozoneConcentration);
				lowerAQIBreakpoint = breakpointAqiDao.getLowerAQIBreakpoint(ozone1hrMin, ozone1hrMax,
						ozoneConcentration);
				higherBreakpoint = breakpointAqiDao.getHigherBreakpoint(ozone1hrMin, ozone1hrMax, ozoneConcentration);
				lowerBreakpoint = breakpointAqiDao.getLowerBreakpoint(ozone1hrMin, ozone1hrMax, ozoneConcentration);
				aqi = calculateAQI(sensorId, ozoneConcentration, higherBreakpoint, lowerBreakpoint, higherAQIBreakpoint,
						lowerAQIBreakpoint);
				return aqi;
			case "nitrogenDioxide":
				double nitrogenDioxideConcentration = maxHourlyNitrogenDioxide(sensorId, dateTime.toLocalDate());
				String nitrogenDioxideMax = "nitrogenDioxideMax";
				String nitrogenDioxideMin = "nitrogenDioxideMin";
				higherAQIBreakpoint = breakpointAqiDao.getHigherAQIBreakpoint(nitrogenDioxideMin, nitrogenDioxideMax,
						nitrogenDioxideConcentration);
				lowerAQIBreakpoint = breakpointAqiDao.getLowerAQIBreakpoint(nitrogenDioxideMin, nitrogenDioxideMax,
						nitrogenDioxideConcentration);
				higherBreakpoint = breakpointAqiDao.getHigherBreakpoint(nitrogenDioxideMin, nitrogenDioxideMax,
						nitrogenDioxideConcentration);
				lowerBreakpoint = breakpointAqiDao.getLowerBreakpoint(nitrogenDioxideMin, nitrogenDioxideMax,
						nitrogenDioxideConcentration);
				aqi = calculateAQI(sensorId, nitrogenDioxideConcentration, higherBreakpoint, lowerBreakpoint,
						higherAQIBreakpoint, lowerAQIBreakpoint);

				return aqi;
			}

		} else {
			throw new InsufficientDataForAqiCalculation();
		}
		return aqi;
	}

	// computes daily individual AQI values for all the pollutants
	@Override
	public int calculateDailyAQI(int sensorId, String pollutant, LocalDate date)
			throws InsufficientDataForAqiCalculation {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(23, 0);
		int aqi = 0;
		if (isDataValid(sensorId, startOfDay, endOfDay)) {
			int higherAQIBreakpoint = 0;
			int lowerAQIBreakpoint = 0;
			double higherBreakpoint = 0;
			double lowerBreakpoint = 0;
			switch (pollutant) {
			case "pm10":
				double pm10Concentration = dailyAveragePM(sensorId, "pm10", date);
				String pm10Max = "pm10Max";
				String pm10Min = "pm10Min";
				higherAQIBreakpoint = breakpointAqiDao.getHigherAQIBreakpoint(pm10Min, pm10Max, pm10Concentration);
				lowerAQIBreakpoint = breakpointAqiDao.getLowerAQIBreakpoint(pm10Min, pm10Max, pm10Concentration);
				higherBreakpoint = breakpointAqiDao.getHigherBreakpoint(pm10Min, pm10Max, pm10Concentration);
				lowerBreakpoint = breakpointAqiDao.getLowerBreakpoint(pm10Min, pm10Max, pm10Concentration);
				aqi = calculateAQI(sensorId, pm10Concentration, higherBreakpoint, lowerBreakpoint, higherAQIBreakpoint,
						lowerAQIBreakpoint);
				return aqi;
			case "pm25":
				double pm25Concentration = dailyAveragePM(sensorId, "pm25", date);
				String pm25Max = "pm25Max";
				String pm25Min = "pm25Min";
				higherAQIBreakpoint = breakpointAqiDao.getHigherAQIBreakpoint(pm25Min, pm25Max, pm25Concentration);
				lowerAQIBreakpoint = breakpointAqiDao.getLowerAQIBreakpoint(pm25Min, pm25Max, pm25Concentration);
				higherBreakpoint = breakpointAqiDao.getHigherBreakpoint(pm25Min, pm25Max, pm25Concentration);
				lowerBreakpoint = breakpointAqiDao.getLowerBreakpoint(pm25Min, pm25Max, pm25Concentration);
				aqi = calculateAQI(sensorId, pm25Concentration, higherBreakpoint, lowerBreakpoint, higherAQIBreakpoint,
						lowerAQIBreakpoint);
				return aqi;
			case "sulfurDioxide":
				double sulfurDioxideConcentration = dailyAverageSulfurDioxide(sensorId, date);
				String sulfurDioxideMax = "sulfurDioxideMax";
				String sulfurDioxideMin = "sulfurDioxideMin";
				higherAQIBreakpoint = breakpointAqiDao.getHigherAQIBreakpoint(sulfurDioxideMin, sulfurDioxideMax,
						sulfurDioxideConcentration);
				lowerAQIBreakpoint = breakpointAqiDao.getLowerAQIBreakpoint(sulfurDioxideMin, sulfurDioxideMax,
						sulfurDioxideConcentration);
				higherBreakpoint = breakpointAqiDao.getHigherBreakpoint(sulfurDioxideMin, sulfurDioxideMax,
						sulfurDioxideConcentration);
				lowerBreakpoint = breakpointAqiDao.getLowerBreakpoint(sulfurDioxideMin, sulfurDioxideMax,
						sulfurDioxideConcentration);
				aqi = calculateAQI(sensorId, sulfurDioxideConcentration, higherBreakpoint, lowerBreakpoint,
						higherAQIBreakpoint, lowerAQIBreakpoint);
				return aqi;
			case "carbonMonoxide":
				double carbonMonoxideConcentration = dailyAverageCarbonMonoxide(sensorId, date);
				String carbonMonoxideMax = "carbonMonoxideMax";
				String carbonMonoxideMin = "carbonMonoxideMin";
				higherAQIBreakpoint = breakpointAqiDao.getHigherAQIBreakpoint(carbonMonoxideMin, carbonMonoxideMax,
						carbonMonoxideConcentration);
				lowerAQIBreakpoint = breakpointAqiDao.getLowerAQIBreakpoint(carbonMonoxideMin, carbonMonoxideMax,
						carbonMonoxideConcentration);
				higherBreakpoint = breakpointAqiDao.getHigherBreakpoint(carbonMonoxideMin, carbonMonoxideMax,
						carbonMonoxideConcentration);
				lowerBreakpoint = breakpointAqiDao.getLowerBreakpoint(carbonMonoxideMin, carbonMonoxideMax,
						carbonMonoxideConcentration);
				aqi = calculateAQI(sensorId, carbonMonoxideConcentration, higherBreakpoint, lowerBreakpoint,
						higherAQIBreakpoint, lowerAQIBreakpoint);
				return aqi;
			case "ozone":
				double ozoneConcentration = dailyAverageOzone(sensorId, date);
				String ozone8hrMax = "ozone8hrMax";
				String ozone8hrMin = "ozone8hrMin";
				higherAQIBreakpoint = breakpointAqiDao.getHigherAQIBreakpoint(ozone8hrMin, ozone8hrMax,
						ozoneConcentration);
				lowerAQIBreakpoint = breakpointAqiDao.getLowerAQIBreakpoint(ozone8hrMin, ozone8hrMax,
						ozoneConcentration);
				higherBreakpoint = breakpointAqiDao.getHigherBreakpoint(ozone8hrMin, ozone8hrMax, ozoneConcentration);
				lowerBreakpoint = breakpointAqiDao.getLowerBreakpoint(ozone8hrMin, ozone8hrMax, ozoneConcentration);
				aqi = calculateAQI(sensorId, ozoneConcentration, higherBreakpoint, lowerBreakpoint, higherAQIBreakpoint,
						lowerAQIBreakpoint);
				return aqi;
			}

		} else {
			throw new InsufficientDataForAqiCalculation();
		}
		return aqi;
	}

	// returns the max AQI value for a day
	@Override
	public int maxDailyAQI(int sensorId, LocalDate date) throws InsufficientDataForAqiCalculation {
		List<String> pollutants = new ArrayList<>(
				Arrays.asList("pm10", "pm25", "sulfurDioxide", "carbonMonoxide", "ozone"));

		List<Integer> aqiList = new ArrayList<>();
		for (String pollutant : pollutants) {
			int aqi = calculateDailyAQI(sensorId, pollutant, date);
			aqiList.add(aqi);
		}
		OptionalInt maxAQI = aqiList.stream().mapToInt(i -> i).max();

		return maxAQI.getAsInt();
	}

	@Override
	public int maxHourlyAQI(int sensorId, LocalDateTime dateTime) throws DataAccessException, NoSuchElementException,
			InsufficientDataForAqiCalculation, HourlyOzoneLowLevelException {
		List<String> pollutants = new ArrayList<>(
				Arrays.asList("pm10", "pm25", "sulfurDioxide", "nitrogenDioxide", "ozone"));
		// check if the ozone and nitrogenDioxide hourly reading falls below minimum
		// possible breakpoints and if so, remove them from the hourly AQI calculation
		double minimumOzoneBreakpoint = 0.125;
		double minimumNitrogenDioxideBreakpoint = 0.65;
		if (hourlyOzone(sensorId, dateTime) < minimumOzoneBreakpoint) {
			pollutants.remove("ozone");
		}
		if (maxHourlyNitrogenDioxide(sensorId, dateTime.toLocalDate()) < minimumNitrogenDioxideBreakpoint) {
			pollutants.remove("nitrogenDioxide");
		}
		List<Integer> aqiList = new ArrayList<>();
		for (String pollutant : pollutants) {
			int aqi = calculateHourlyAQI(sensorId, pollutant, dateTime);
			aqiList.add(aqi);

		}
		OptionalInt maxAQI = aqiList.stream().mapToInt(i -> i).max();

		return maxAQI.getAsInt();
	}

	@Override
	public List<Double> getDailyOzone(int sensorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		try {
			return measurementDao.getDailyOzone(sensorId, startDateTime, startDateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public List<Double> getDailyPm10(int sensorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {

		try {
			return measurementDao.getDailyPm10(sensorId, startDateTime, startDateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public List<Double> getDailyPm25(int sensorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		try {
			return measurementDao.getDailyPm25(sensorId, startDateTime, startDateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public List<Double> getDailySulfurDioxide(int sensorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		try {
			return measurementDao.getDailySulfurDioxide(sensorId, startDateTime, startDateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public List<Double> getDailyCarbonMonoxide(int sensorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		try {
			return measurementDao.getDailyCarbonMonoxide(sensorId, startDateTime, startDateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public List<Double> getDailyNitrogenDioxide(int sensorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		try {
			return measurementDao.getDailyNitrogenDioxide(sensorId, startDateTime, startDateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Double getHourlyOzone(int sensorId, LocalDateTime dateTime) {
		try {
			return measurementDao.getHourlyOzone(sensorId, dateTime);
		} catch (DataAccessException e) {
			throw e;
		}

	}

	@Override
	public Double getHourlyPm10(int sensorId, LocalDateTime dateTime) {
		try {
			return measurementDao.getHourlyPm10(sensorId, dateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Double getHourlyPm25(int sensorId, LocalDateTime dateTime) {
		try {
			return measurementDao.getHourlyPm25(sensorId, dateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Double getHourlySulfurDioxide(int sensorId, LocalDateTime dateTime) {
		try {
			return measurementDao.getHourlySulfurDioxide(sensorId, dateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Double getHourlyNitrogenDioxide(int sensorId, LocalDateTime dateTime) {
		try {
			return measurementDao.getHourlyNitrogenDioxide(sensorId, dateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public List<Double> getPollutantByNameAndSensor(double pollutantName, int sensorId, LocalDateTime startDateTime,
			LocalDateTime endDateTime) {
		try {
			return measurementDao.getPollutantByNameAndSensor(pollutantName, sensorId, startDateTime, startDateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public List<Measurement> getHourlyMeasurements(int sensorId, LocalDateTime dateTime) {
		try {
			return measurementDao.getHourlyMeasurements(sensorId, dateTime);
		} catch (DataAccessException e) {
			throw e;
		}
	}
	
	@Override
	public Map<Sensor, Integer> getHourlyAQIForAllSensors(LocalDateTime dateTime) throws Exception {
		List<Sensor> sensors = sensorService.getAllSensors();
		Map<Sensor, Integer> aqiValues = new HashMap<>();
		for (Sensor s : sensors) {

			Integer aqi=0;
			try {
				aqi = maxHourlyAQI(s.getId(), dateTime);
				aqiValues.put(s, aqi);
			} catch (DataAccessException | NoSuchElementException | InsufficientDataForAqiCalculation
					| HourlyOzoneLowLevelException e) {
				throw e;
			}

		}

		return aqiValues;

	}
	 

}
