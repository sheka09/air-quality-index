package org.be.airqualitymonitoring.dao;

import java.util.List;

import org.be.airqualitymonitoring.entity.Sensor;
import org.be.airqualitymonitoring.error.SensorAlreadyExistsException;
import org.springframework.dao.DataAccessException;

public interface SensorDao {
		
	public Sensor saveSensor(Sensor sensor) throws SensorAlreadyExistsException,DataAccessException;
	
	public Sensor getSensorById(int sensorId) throws DataAccessException;
	
	public Sensor updateSensor(int sensorId,Sensor sensor) throws DataAccessException;
	
	public Sensor deleteBySensorId(int sensorId) throws DataAccessException;
	
	public List<Sensor> getAllSensors() throws DataAccessException;
	
	public List<Sensor> getSensorsByZipcode(String zipCode) throws DataAccessException;
	
	
	
	
	

}
