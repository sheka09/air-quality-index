package org.be.airqualitymonitoring.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.be.airqualitymonitoring.AqiApplication;
import org.be.airqualitymonitoring.entity.BreakpointAqi;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

@Repository
public class BreakpointAqiDaoImpl implements BreakpointAqiDao {
	private static final Logger LOGGER = LogManager.getLogger(AqiApplication.class);

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
    
	public BreakpointAqi saveBreakpointAQI(BreakpointAqi breakpointAqi) throws DataAccessException{
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		entityManager.persist(breakpointAqi);
		entityManager.close();
		transaction.commit();
		return breakpointAqi;
	}

//returns the AQI object where the concentration of the pollutant falls
	@Override
	public BreakpointAqi updateBreakpointAQI(int id, BreakpointAqi breakpointAqi) throws DataAccessException
			 {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		breakpointAqi.setId(id);
		entityManager.merge(breakpointAqi);
		entityManager.close();
		transaction.commit();
	    LOGGER.info("Breakpoint AQI with id: "+id+ "updated successfully");

		return breakpointAqi;
	}

	@Override
	public BreakpointAqi deleteById(int id) throws DataAccessException{
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		BreakpointAqi breakpointAqi = entityManager.find(BreakpointAqi.class, id);

		
		entityManager.remove(breakpointAqi);
		entityManager.close();

	    transaction.commit();
	    LOGGER.info("Breakpoint AQI with id: "+id+ "deleted successfully");
		return breakpointAqi;
	}


	
	@Override
	public BreakpointAqi getBreakpointAQI(String pollutant, double concentration) throws DataAccessException {

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		String maxPollutant = pollutant + "Max";
		String minPollutant = pollutant + "Min";

		BreakpointAqi breakpointAqi = (BreakpointAqi) entityManager.createQuery("from BreakpointAQI b where " + "b."
				+ maxPollutant + " <=:concentration and b." + minPollutant + " >=: concentration")
				.setParameter("concentration", concentration);

		entityManager.close();

		transaction.commit();
		LOGGER.info("The breakpoint AQI for " + pollutant + " is returned successfully.");
		return breakpointAqi;

	}


	@Override
	public BreakpointAqi getBreakpointAQIById(int id) throws DataAccessException{
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		BreakpointAqi breakpointAqi = (BreakpointAqi) entityManager.find(BreakpointAqi.class, id);
	 
       LOGGER.info("my id: " + breakpointAqi.getId());
		entityManager.close();
        transaction.commit();

		return breakpointAqi;
	}

	@Override
	public Integer getHigherAQIBreakpoint(String pollutantMin,String pollutantMax, double concentration) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
        
		TypedQuery<Integer> query= entityManager.createQuery("select b.aqiMax from BreakpointAqi b where " 
				+"b."+pollutantMax+" >=: concentration and b."+pollutantMin+" <=: concentration",Integer.class).setParameter("concentration", concentration);
		Integer higherAqi=query.getSingleResult();
	
		entityManager.close();

        transaction.commit();
		return higherAqi;
	}

	@Override
	public Integer getLowerAQIBreakpoint(String pollutantMin,String pollutantMax, double concentration) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
        
		TypedQuery<Integer> query= entityManager.createQuery("select b.aqiMin from BreakpointAqi b where " 
				+"b."+pollutantMax+" >=: concentration and b."+pollutantMin+" <=: concentration",Integer.class).setParameter("concentration", concentration);
		
		Integer lowerAqi=query.getSingleResult();
		
		entityManager.close();

        transaction.commit();
		return lowerAqi;
	}

	@Override
	public Double getHigherBreakpoint(String pollutantMin,String pollutantMax, double concentration) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Double> query= entityManager.createQuery("select b."+pollutantMax+" from BreakpointAqi b where " 
				+"b."+pollutantMax+" >=: concentration and b."+pollutantMin+" <=: concentration",Double.class).setParameter("concentration", concentration);
		
		Double higherBreakpoint=query.getSingleResult();
	
		entityManager.close();

        transaction.commit();
		return higherBreakpoint;

	}

	@Override
	public Double getLowerBreakpoint(String pollutantMin,String pollutantMax, double concentration) throws DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

	
		TypedQuery<Double> query= entityManager.createQuery("select b."+pollutantMin+" from BreakpointAqi b where " 
				+"b."+pollutantMax+" >=: concentration and b."+pollutantMin+" <=: concentration",Double.class).setParameter("concentration", concentration);
		
		Double lowerBreakpoint=query.getSingleResult();
	
		entityManager.close();

        transaction.commit();
		return lowerBreakpoint;
	}

	
}
