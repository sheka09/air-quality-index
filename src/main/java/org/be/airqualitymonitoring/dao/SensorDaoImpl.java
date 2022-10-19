package org.be.airqualitymonitoring.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.be.airqualitymonitoring.entity.Sensor;
import org.be.airqualitymonitoring.error.SensorAlreadyExistsException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class SensorDaoImpl implements SensorDao {
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@Override
	public Sensor saveSensor(Sensor sensor) throws SensorAlreadyExistsException, DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(sensor);
		entityManager.close();
		transaction.commit();
		return sensor;
	}

	@Override
	public Sensor getSensorById(int id) throws DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Sensor sensor = entityManager.find(Sensor.class, id);
		entityManager.close();
		transaction.commit();
		return sensor;
	}

	@Override
	public Sensor updateSensor(int id, Sensor sensor) throws DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Sensor existing_Sensor = entityManager.find(Sensor.class, id);
		sensor.setId(existing_Sensor.getId());
		entityManager.merge(sensor);
		entityManager.close();

		return sensor;
	}

	@Override
	public Sensor deleteBySensorId(int id) throws DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Sensor sensor = entityManager.find(Sensor.class, id);
		entityManager.remove(sensor);
		transaction.commit();

		return sensor;
	}

	@Override
	public List<Sensor> getAllSensors() throws DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		TypedQuery<Sensor> query = entityManager.createQuery(" from Sensor s", Sensor.class);

		List<Sensor> sensors = query.getResultList();
		entityManager.close();

		return sensors;
	}

	@Override
	public List<Sensor> getSensorsByZipcode(String zipcode) throws DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Sensor> query = entityManager
				.createQuery("select s from Sensor s where s.zipcode=:zipcode", Sensor.class)
				.setParameter("zipcode", zipcode);

		List<Sensor> sensors = query.getResultList();
		entityManager.close();

		return sensors;
	}

}
