package org.be.airqualitymonitoring.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.be.airqualitymonitoring.entity.Measurement;

public interface MeasurementDao {

    public Measurement saveMeasurement(Measurement measurement);
    
    public Measurement updateMeasurement(int id,Measurement measurement);
    
    public Measurement deleteMeasurement(int id);
    
    public Measurement getMeasurement(int id);
    	
	public List<Measurement> getMeasurements(int sensorId,LocalDateTime startDateTime, LocalDateTime endDateTime);
	
	public List<Measurement> getHourlyMeasurements(int sensorId,LocalDateTime dateTime);
	
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

	public List<Double> getPollutantByNameAndSensor(double pollutantName,int sensorId,LocalDateTime startDateTime,LocalDateTime endDateTime);



}

 
