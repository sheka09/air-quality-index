package org.be.airqualitymonitoring.controller;

import java.util.List;

import org.be.airqualitymonitoring.entity.Sensor;
import org.be.airqualitymonitoring.error.SensorAlreadyExistsException;
import org.be.airqualitymonitoring.error.SensorNotFoundException;
import org.be.airqualitymonitoring.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
public class SensorController {
	@Autowired
	private SensorService sensorService;

	@PostMapping("/sensor")
	public ResponseEntity<?> saveSensor(@RequestBody Sensor sensor){
		try {
			return new ResponseEntity<Sensor>(sensorService.saveSensor(sensor),HttpStatus.OK);
		}catch(SensorAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
		}catch(DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@GetMapping("/sensor/{id}")
	public ResponseEntity<?> getSensorById(@PathVariable int id){
		try {
			return new ResponseEntity<Sensor>(sensorService.getSensorById(id),HttpStatus.OK);
		}catch(DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@PutMapping("/sensor/{id}")
	public ResponseEntity<?> updateSensor(@PathVariable int id,@RequestBody Sensor sensor){
		try {
			return new ResponseEntity<Sensor>(sensorService.updateSensor(id, sensor),HttpStatus.OK);
		}catch(DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/sensor/{id}")
	public ResponseEntity<?> deleteBySensorId(@PathVariable int id){
		try {
			return new ResponseEntity<Sensor>(sensorService.deleteBySensorId(id),HttpStatus.OK);
		}catch(DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/sensors")
	public ResponseEntity<?> getAllSensors(){
		try {
			return new ResponseEntity<List<Sensor>>(sensorService.getAllSensors(),HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

		}
		
	}
	
	@GetMapping("/sensors/zipcode")
	public ResponseEntity<?> getSensorsByZipcode(@RequestBody String zipCode){
		try {
			return new ResponseEntity<List<Sensor>>(sensorService.getSensorsByZipcode(zipCode),HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
