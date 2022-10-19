package org.be.airqualitymonitoring.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.be.airqualitymonitoring.AqiApplication;
import org.be.airqualitymonitoring.dao.SensorDao;
import org.be.airqualitymonitoring.entity.Sensor;
import org.be.airqualitymonitoring.error.SensorAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SensorServiceImpl implements SensorService {

	private static final Logger LOGGER = LogManager.getLogger(AqiApplication.class);

	@Autowired
	private SensorDao sensorDao;

	@Override
	public Sensor saveSensor(Sensor sensor) throws SensorAlreadyExistsException {
		try {
		return sensorDao.saveSensor(sensor);
	}catch(DataAccessException e) {
		throw e;
	}catch(SensorAlreadyExistsException e) {
		throw new SensorAlreadyExistsException();
	}
	}

	@Override
	public Sensor getSensorById(int sensorId) {
		try{
			return sensorDao.getSensorById(sensorId);
		}catch(DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Sensor updateSensor(int sensorId, Sensor sensor) throws DataAccessException {
		try{
			return sensorDao.updateSensor(sensorId, sensor);
		}catch(DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Sensor deleteBySensorId(int sensorId) throws DataAccessException {
		try{
			return sensorDao.deleteBySensorId(sensorId);
		}catch(DataAccessException e) {
			throw e;
		}
	}

	@Override
	public List<Sensor> getAllSensors() throws DataAccessException{
		try{
			return sensorDao.getAllSensors();
		}catch(DataAccessException e) {
			throw e;
		}
	}

	@Override
	public List<Sensor> getSensorsByZipcode(String zipCode) throws DataAccessException{
		try{
			return sensorDao.getSensorsByZipcode(zipCode);
		}catch(DataAccessException e) {
			throw e;
		}
	}
}
