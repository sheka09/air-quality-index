package org.be.airqualitymonitoring.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.be.airqualitymonitoring.entity.Measurement;
import org.be.airqualitymonitoring.entity.Sensor;
import org.be.airqualitymonitoring.error.HourlyOzoneLowLevelException;
import org.be.airqualitymonitoring.error.InsufficientDataForAqiCalculation;
import org.springframework.dao.DataAccessException;

public interface MeasurementService {

	public Measurement saveMeasurement(Measurement measurement);

	public Measurement updateMeasurement(int id, Measurement measurement);

	public Measurement deleteMeasurement(int id);

	public Measurement getMeasurement(int id) ;
	
	public List<Double> getPollutantByNameAndSensor(double pollutantName,int sensorId,LocalDateTime startDateTime,LocalDateTime endDateTime);

//	public List<Double> getMeasurementsByPollutant(String pollutantName, LocalDateTime start, LocalDateTime end);

	public List<Measurement> getMeasurements(int sensorId,LocalDateTime start, LocalDateTime end) ;
	
	public List<Measurement> getHourlyMeasurements(int sensorId,LocalDateTime dateTime);
	
	public boolean isDataValid(int sensorId,LocalDateTime startDateTime, LocalDateTime endDateTime);

	public int calculateAQI(int sensorId,double concentration, double higherBreakpoint, double lowerBreakpoint, int AQIHigherBreakpoint,
			int AQILowerBreakpoint);

	public double dailyAverageOzone(int sensorId,LocalDate date) throws InsufficientDataForAqiCalculation;

	public double dailyAveragePM(int sensorId,String pollutantName, LocalDate date);

	public double hourlyPM(int sensorId,String pollutantName, LocalDateTime dateTime) throws InsufficientDataForAqiCalculation;

	public double dailyAverageSulfurDioxide(int sensorId,LocalDate date);

	public double maxHourlySulfurDioxide(int sensorId,LocalDate date);

	public double dailyAverageCarbonMonoxide(int sensorId,LocalDate date) throws InsufficientDataForAqiCalculation;

	public double maxHourlyNitrogenDioxide(int sensorId,LocalDate date);

	public Double hourlyOzone(int sensorId,LocalDateTime dateTime)
			throws InsufficientDataForAqiCalculation, HourlyOzoneLowLevelException;
	
	public boolean isSufficientDataAvailable(int sensorId,String pollutantName, LocalDateTime startDateTime,
			LocalDateTime endDateTime);
	
	public int calculateHourlyAQI(int sensorId,String pollutant, LocalDateTime dateTime) throws NoSuchElementException, InsufficientDataForAqiCalculation, DataAccessException, HourlyOzoneLowLevelException;
	
	public int calculateDailyAQI(int sensorId,String pollutant, LocalDate date) throws InsufficientDataForAqiCalculation;
	
	public int maxDailyAQI(int sensorId, LocalDate date) throws InsufficientDataForAqiCalculation;
	
	public int maxHourlyAQI(int sensorId,LocalDateTime dateTime) throws DataAccessException, NoSuchElementException, InsufficientDataForAqiCalculation, HourlyOzoneLowLevelException;
	
	public List<Double> getDailyOzone(int sensorId,LocalDateTime startDateTime,LocalDateTime endDateTime);

	public List<Double> getDailyPm10(int sensorId,LocalDateTime startDateTime,LocalDateTime endDateTime);

	public List<Double> getDailyPm25(int sensorId,LocalDateTime startDateTime,LocalDateTime endDateTime);

	public List<Double> getDailySulfurDioxide(int sensorId,LocalDateTime startDateTime,LocalDateTime endDateTime);

	public List<Double> getDailyCarbonMonoxide(int sensorId,LocalDateTime startDateTime,LocalDateTime endDateTime);
	
	public List<Double> getDailyNitrogenDioxide(int sensorId,LocalDateTime startDateTime,LocalDateTime endDateTime);
		
	public Double getHourlyOzone(int sensorId,LocalDateTime dateTime);

	public Double getHourlyPm10(int sensorId,LocalDateTime dateTime);

	public Double getHourlyPm25(int sensorId,LocalDateTime dateTime);

	public Double getHourlySulfurDioxide(int sensorId,LocalDateTime dateTime);

	public Double getHourlyNitrogenDioxide(int sensorId,LocalDateTime dateTime);

	public Map<Sensor, Integer> getHourlyAQIForAllSensors(LocalDateTime dateTime) throws Exception;

	}
