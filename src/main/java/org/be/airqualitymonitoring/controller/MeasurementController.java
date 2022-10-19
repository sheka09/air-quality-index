package org.be.airqualitymonitoring.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.be.airqualitymonitoring.entity.Measurement;
import org.be.airqualitymonitoring.entity.Sensor;
import org.be.airqualitymonitoring.error.HourlyOzoneLowLevelException;
import org.be.airqualitymonitoring.error.InsufficientDataForAqiCalculation;
import org.be.airqualitymonitoring.error.MeasurementAlreadyExistsException;
import org.be.airqualitymonitoring.error.MeasurementNotFoundException;
import org.be.airqualitymonitoring.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeasurementController {
	@Autowired
	private MeasurementService measurementService;

	@PostMapping("/measurements")
	public ResponseEntity<?> saveMeasurement(@RequestBody Measurement measurement) {
		try {
			return new ResponseEntity<Measurement>(measurementService.saveMeasurement(measurement), HttpStatus.CREATED);

		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/measurements/{id}")
	public ResponseEntity<?> updateMeasurement(@PathVariable int id, @RequestBody Measurement measurement) {
		try {
			return new ResponseEntity<Measurement>(measurementService.updateMeasurement(id, measurement),
					HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/measurements/{id}")
	public ResponseEntity<?> deleteMeasurement(@PathVariable int id) {
		try {
			return new ResponseEntity<Measurement>(measurementService.deleteMeasurement(id), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/measurements/{id}")
	public ResponseEntity<?> getMeasurement(@PathVariable int id) {
		try {
			return new ResponseEntity<Measurement>(measurementService.getMeasurement(id), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	

	@GetMapping("measurements/ozone/{id}/{start}/{end}")
	public ResponseEntity<?> getDailyOzone(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		try {
			return new ResponseEntity<List<Double>>(measurementService.getDailyOzone(sensorId,start, end), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("measurements/pm10/{id}/{start}/{end}")
	public ResponseEntity<?> getDailypm10( @PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		try {
			return new ResponseEntity<List<Double>>(measurementService.getDailyPm10(sensorId,start, end), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("measurements/pm25/{id}/{start}/{end}")
	public ResponseEntity<?> getDailyPm25(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		try {
			return new ResponseEntity<List<Double>>(measurementService.getDailyPm25(sensorId,start, end), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("measurements/so2/{id}/{start}/{end}")
	public ResponseEntity<?> getDailySulfurDioxide(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		try {
			return new ResponseEntity<List<Double>>(measurementService.getDailySulfurDioxide(sensorId,start, end),
					HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("measurements/no2/{id}/{start}/{end}")
	public ResponseEntity<?> getDailyNitrogenDioxide(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		try {
			return new ResponseEntity<List<Double>>(measurementService.getDailyNitrogenDioxide(sensorId,start, end),
					HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("measurements/co/{id}/{start}/{end}")
	public ResponseEntity<?> getDailyCarbonMonoxide(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		try {
			return new ResponseEntity<List<Double>>(measurementService.getDailyCarbonMonoxide(sensorId,start, end),
					HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/measurements/{id}/{start}/{end}")
	public ResponseEntity<?> getMeasurements(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		try {
			return new ResponseEntity<List<Measurement>>(measurementService.getMeasurements(sensorId,start, end), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	
	@GetMapping("/measurements/hourly/{id}/{start}")
	public ResponseEntity<?> getHourlyMeasurements(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
		try {
			return new ResponseEntity<List<Measurement>>(measurementService.getHourlyMeasurements(sensorId,dateTime), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}


	@GetMapping("/measurements/ozone-daily/{id}/{date}")
	public ResponseEntity<?> dailyAverageOzone(@PathVariable("id") int sensorId,

			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		try {
			return new ResponseEntity<Double>(measurementService.dailyAverageOzone(sensorId,date), HttpStatus.OK);
		} catch (InsufficientDataForAqiCalculation e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("/measurements/pm-daily/{id}{pollutant}/{date}")
	public ResponseEntity<?> dailyAveragePM(@PathVariable("id") int sensorId,@PathVariable("pollutant") String pollutantName,

			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		try {
			return new ResponseEntity<Double>(measurementService.dailyAveragePM(sensorId,pollutantName, date), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/measurements/pm-hourly/{id}/{pollutant}/{dateTime}")
	public ResponseEntity<?> hourlyPM(@PathVariable("id") int sensorId,@PathVariable("pollutant") String pollutantName,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
		try {
			return new ResponseEntity<Double>(measurementService.hourlyPM(sensorId,pollutantName, dateTime), HttpStatus.OK);
		} catch (InsufficientDataForAqiCalculation e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // HttpStatus code for
																									// insufficientdata
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("/measurements/hourly/ozone/{id}/{dateTime}")
	public ResponseEntity<?> getHourlyOzone(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {

		try {
			return new ResponseEntity<Double>(measurementService.getHourlyOzone(sensorId,dateTime), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/measurements/hourly/pm10/{id}/{dateTime}")
	public ResponseEntity<?> getHourlyPm10(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {

		try {
			return new ResponseEntity<Double>(measurementService.getHourlyPm10(sensorId,dateTime), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/measurements/hourly/{id}/pm25/{dateTime}")
	public ResponseEntity<?> getHourlyPm25(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {

		try {
			return new ResponseEntity<Double>(measurementService.getHourlyPm25(sensorId,dateTime), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}


	@GetMapping("/measurements/hourly/no2/{id}/{dateTime}")
	public ResponseEntity<?> getHourlyNitrogenDioxide(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {

		try {
			return new ResponseEntity<Double>(measurementService.getHourlyNitrogenDioxide(sensorId,dateTime), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/measurements/hourly/so2/{id}/{dateTime}")
	public ResponseEntity<?> getHourlySulfurDioxide(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {

		try {
			return new ResponseEntity<Double>(measurementService.getHourlySulfurDioxide(sensorId,dateTime), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/measurements/so2-daily/{id}/{date}")
	public ResponseEntity<?> dailyAverageSulfurDioxide(@PathVariable("id") int sensorId,

			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		try {
			return new ResponseEntity<Double>(measurementService.dailyAverageSulfurDioxide(sensorId,date), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/measurements/so2-hourly/{id}/{date}")
	public ResponseEntity<?> maxHourlySulfurDioxide(@PathVariable("id") int sensorId,

			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		try {
			return new ResponseEntity<Double>(measurementService.maxHourlySulfurDioxide(sensorId,date), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/measurements/co-daily/{id}/{date}")
	public ResponseEntity<?> dailyAverageCarbonMonoxide(@PathVariable("id") int sensorId,

			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		try {
			return new ResponseEntity<Double>(measurementService.dailyAverageCarbonMonoxide(sensorId,date), HttpStatus.OK);
		} catch (InsufficientDataForAqiCalculation e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("/measurements/no2-daily/{id}/{date}")
	public ResponseEntity<?> maxHourlyNitrogenDioxide(@PathVariable("id") int sensorId,

			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		try {
			return new ResponseEntity<Double>(measurementService.maxHourlyNitrogenDioxide(sensorId,date), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/measurements/ozone-hourly/{id}/{dateTime}")
	public ResponseEntity<?> hourlyOzone(@PathVariable("id") int sensorId,

			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
		try {
			return new ResponseEntity<Double>(measurementService.hourlyOzone(sensorId,dateTime), HttpStatus.OK);
		} catch (InsufficientDataForAqiCalculation e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (HourlyOzoneLowLevelException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("measurements/max-daily-aqi/{id}/{date}")
	public ResponseEntity<?> getmaxDailyAQI(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		try {
			return new ResponseEntity<Integer>(measurementService.maxDailyAQI(sensorId,date), HttpStatus.OK);
		} catch (InsufficientDataForAqiCalculation e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NO_CONTENT);
		}

	}

	@GetMapping("measurements/max-hourly-aqi/{id}/{dateTime}")
	public ResponseEntity<?> getmaxHourlyAQI(@PathVariable("id") int sensorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
		try {
			return new ResponseEntity<Integer>(measurementService.maxHourlyAQI(sensorId,dateTime), HttpStatus.OK);
		} catch (InsufficientDataForAqiCalculation e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NO_CONTENT);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (NoSuchElementException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NO_CONTENT);

		} catch (HourlyOzoneLowLevelException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NO_CONTENT);

		}

	}
	
	@GetMapping("measurements/all-aqi/{dateTime}")
	public ResponseEntity<?> getHourlyAQIForAllSensors(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {

		try {
			return new ResponseEntity<Map<Sensor, Integer>>(measurementService.getHourlyAQIForAllSensors(dateTime),
					HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (NoSuchElementException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NO_CONTENT);

		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NO_CONTENT);

		}

	}
}
