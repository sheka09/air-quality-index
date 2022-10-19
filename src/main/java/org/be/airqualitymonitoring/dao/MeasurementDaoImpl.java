package org.be.airqualitymonitoring.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.be.airqualitymonitoring.AqiApplication;
import org.be.airqualitymonitoring.entity.Measurement;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementDaoImpl implements MeasurementDao {

	private static final Logger LOGGER = LogManager.getLogger(AqiApplication.class);

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@Override
	public Measurement saveMeasurement(Measurement measurement) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		entityManager.persist(measurement);
		transaction.commit();
		entityManager.close();
		LOGGER.info("Measurement with id " + measurement.getId() + " is successfuly saved");
		return measurement;
	}

	@Override
	public Measurement updateMeasurement(int id, Measurement measurement) throws DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		measurement.setId(id);
		entityManager.merge(measurement);
		transaction.commit();
		entityManager.close();

		LOGGER.info("Measurement with id " + id + " was successfuly updated");

		return measurement;
	}

	@Override
	public Measurement deleteMeasurement(int id) throws DataAccessException {

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		Measurement measurement = entityManager.find(Measurement.class, id);
		entityManager.remove(measurement);
		transaction.commit();
		entityManager.close();

		LOGGER.info("Measurement with id " + id + " was successfuly deleted");
		return measurement;
	}

	@Override
	public Measurement getMeasurement(int id) throws DataAccessException {

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Measurement measurement = entityManager.find(Measurement.class, id);
		transaction.commit();
		entityManager.close();

		LOGGER.info("Measurement with id " + id + " is returned");
		return measurement;
	}

	@Override
	public List<Measurement> getMeasurements(int sensorId,LocalDateTime startDateTime, LocalDateTime endDateTime)
			throws DataAccessException {

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Measurement> query = entityManager
				.createQuery(
						"select m from Measurement m left join m.sensor s where s.id=:sensorId and m.dateTime>=:startDateTime and m.dateTime<=:endDateTime",
						Measurement.class)
				.setParameter("startDateTime", startDateTime).setParameter("endDateTime", endDateTime).setParameter("sensorId",sensorId);
		List<Measurement> measurements = query.getResultList();

		transaction.commit();
		entityManager.close();

		LOGGER.info("List of measurements is returned.");
		return measurements;
	}

	@Override
	public List<Double> getDailyOzone(int sensorId,LocalDateTime startDateTime, LocalDateTime endDateTime) throws DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query = entityManager.createQuery(
				" select m.ozone from Measurement m left join m.sensor s where s.id=:sensorId and m.dateTime>=:startDateTime and m.dateTime<=:endDateTime",
				Double.class).setParameter("startDateTime", startDateTime).setParameter("endDateTime", endDateTime).setParameter("sensorId",sensorId);
		List<Double> measurements = query.getResultList();
		transaction.commit();
		entityManager.close();
		return measurements;
	}

	@Override
	public List<Double> getDailyPm10(int sensorId,LocalDateTime startDateTime, LocalDateTime endDateTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query = entityManager.createQuery(
				" select m.pm10 from Measurement m left join m.sensor s where s.id=:sensorId and m.dateTime>=:startDateTime and m.dateTime<=:endDateTime",
				Double.class).setParameter("startDateTime", startDateTime).setParameter("endDateTime", endDateTime).setParameter("sensorId",sensorId);
		List<Double> measurements = query.getResultList();
		transaction.commit();
		entityManager.close();
		return measurements;
	}

	@Override
	public List<Double> getDailyPm25(int sensorId,LocalDateTime startDateTime, LocalDateTime endDateTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query = entityManager.createQuery(
				" select m.pm25 from Measurement m left join m.sensor s where s.id=:sensorId and m.dateTime>=:startDateTime and m.dateTime<=:endDateTime",
				Double.class).setParameter("startDateTime", startDateTime).setParameter("endDateTime", endDateTime).setParameter("sensorId",sensorId);
		List<Double> measurements = query.getResultList();
		transaction.commit();
		entityManager.close();
		return measurements;
	}

	@Override
	public List<Double> getDailySulfurDioxide(int sensorId,LocalDateTime startDateTime, LocalDateTime endDateTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query = entityManager.createQuery(
				" select m.sulfurDioxide from Measurement m left join m.sensor s where s.id=:sensorId and m.dateTime>=:startDateTime and m.dateTime<=:endDateTime",
				Double.class).setParameter("startDateTime", startDateTime).setParameter("endDateTime", endDateTime).setParameter("sensorId",sensorId);
		List<Double> measurements = query.getResultList();
		transaction.commit();
		entityManager.close();
		return measurements;
	}

	@Override
	public List<Double> getDailyCarbonMonoxide(int sensorId,LocalDateTime startDateTime, LocalDateTime endDateTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query = entityManager.createQuery(
				" select m.carbonMonoxide from Measurement m left join m.sensor s where s.id=:sensorId and m.dateTime>=:startDateTime and m.dateTime<=:endDateTime",
				Double.class).setParameter("startDateTime", startDateTime).setParameter("endDateTime", endDateTime).setParameter("sensorId",sensorId);
		List<Double> measurements = query.getResultList();
		transaction.commit();
		entityManager.close();
		return measurements;
	}

	@Override
	public List<Double> getDailyNitrogenDioxide(int sensorId,LocalDateTime startDateTime, LocalDateTime endDateTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query = entityManager.createQuery(
				" select m.nitrogenDioxide from Measurement m left join m.sensor s where s.id=:sensorId and m.dateTime>=:startDateTime and m.dateTime<=:endDateTime",
				Double.class).setParameter("startDateTime", startDateTime).setParameter("endDateTime", endDateTime).setParameter("sensorId",sensorId);
		
		List<Double> measurements = query.getResultList();
		transaction.commit();
		entityManager.close();
		return measurements;
	}

	@Override
	public Double getHourlyOzone(int sensorId,LocalDateTime dateTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query = entityManager.createQuery(
				" select m.ozone from Measurement m left join m.sensor s where s.id=:sensorId and m.dateTime=:dateTime",
				Double.class).setParameter("dateTime", dateTime).setParameter("sensorId",sensorId);
		Double measurement = query.getSingleResult();
		transaction.commit();
		entityManager.close();
		return measurement;
	}

	@Override
	public Double getHourlyPm10(int sensorId,LocalDateTime dateTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query = entityManager.createQuery(
				" select m.pm10 from Measurement m left join m.sensor s where s.id=:sensorId and  m.dateTime=:dateTime",
				Double.class).setParameter("dateTime", dateTime).setParameter("sensorId",sensorId);
		Double measurement = query.getSingleResult();
		transaction.commit();
		entityManager.close();
		return measurement;
	}

	@Override
	public Double getHourlyPm25(int sensorId,LocalDateTime dateTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query = entityManager.createQuery(
				" select m.pm25 from Measurement m left join m.sensor s where s.id=:sensorId and m.dateTime=:dateTime",
				Double.class).setParameter("dateTime", dateTime).setParameter("sensorId",sensorId);
		Double measurement = query.getSingleResult();
		transaction.commit();
		entityManager.close();
		return measurement;
	}

	@Override
	public Double getHourlySulfurDioxide(int sensorId,LocalDateTime dateTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query = entityManager.createQuery(
				" select m.sulfurDioxide from Measurement m left join m.sensor s where s.id=:sensorId and m.dateTime=:dateTime",
				Double.class).setParameter("dateTime", dateTime).setParameter("sensorId",sensorId);
		Double measurement = query.getSingleResult();
		transaction.commit();
		entityManager.close();
		return measurement;
	}

	@Override
	public Double getHourlyNitrogenDioxide(int sensorId,LocalDateTime dateTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query = entityManager.createQuery(
				" select m.nitrogenDioxide from Measurement m left join m.sensor s where s.id=:sensorId and m.dateTime=:dateTime",
				Double.class).setParameter("dateTime", dateTime).setParameter("sensorId",sensorId);
		Double measurement = query.getSingleResult();
		transaction.commit();
		entityManager.close();
		return measurement;
	}
	
	@Override
	public List<Double> getPollutantByNameAndSensor(double pollutantName, int sensorId, LocalDateTime startDateTime,
			LocalDateTime endDateTime) {
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
        TypedQuery<Double> query=entityManager.createQuery("select m.pollutantName from Measurement m where m.sensor=:sensorId and m.dateTime>=:startDateTime and m.dateTime<=:endDateTime",Double.class)
        		.setParameter("pollutantName",pollutantName).setParameter("sensorId",sensorId).setParameter("startDateTime",startDateTime).setParameter("endDateTime",endDateTime);		
        List<Double> measurementBySensor=query.getResultList();
		entityManager.close();

        transaction.commit();
		
		return measurementBySensor;
	}

	@Override
	public List<Measurement> getHourlyMeasurements(int sensorId,LocalDateTime dateTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Measurement> query = entityManager
				.createQuery(
						"select m from Measurement m left join m.sensor s where s.id=:sensorId and  m.dateTime =:dateTime",
						Measurement.class)
				.setParameter("dateTime", dateTime).setParameter("sensorId",sensorId);
		List<Measurement> measurements = query.getResultList();

		transaction.commit();
		entityManager.close();

		LOGGER.info("List of hourly measurements is returned.");
		return measurements;
	}

	 

}
