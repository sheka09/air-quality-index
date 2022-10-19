package io.aqi.daoImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

import org.be.airqualitymonitoring.dao.BreakpointAqiDao;
import org.be.airqualitymonitoring.dao.BreakpointAqiDaoImpl;
import org.be.airqualitymonitoring.error.MeasurementOutOfRangeException;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
class BreakpointAqiDaoImplTest {
	
	@Autowired
	private BreakpointAqiDaoImpl breakpointAqiDaoImpl;
	@Mock
    private EntityManager entityManager;
	@Mock
	private EntityManagerFactory entityManagerFactory;
	
	@Test
	void testSaveBreakpointAQI() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateBreakpointAQI() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteById() {
		fail("Not yet implemented");
	}

	@Test
	void testGetBreakpointAQI() {
		fail("Not yet implemented");
	}

	/*
	 * @Test void testInRange() throws MeasurementOutOfRangeException {
	 * 
	 * boolean expected=true; BreakpointAqiDao bdo=new BreakpointAqiDaoImpl();
	 * String pollutant="carbonMonoxide"; double concentration=50.3; boolean
	 * actual=bdo.inRange(pollutant, concentration); // Query
	 * queryMock=Mockito.mock(Query.class);
	 * when(breakpointAqiDaoImpl.inRange(pollutant,
	 * concentration)).thenReturn(true); assertEquals(expected,actual); }
	 */

	@Test
	void testGetBreakpointAQIById() {
		fail("Not yet implemented");
	}

}
