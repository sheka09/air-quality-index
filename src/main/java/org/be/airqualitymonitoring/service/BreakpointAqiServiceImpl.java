package org.be.airqualitymonitoring.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.be.airqualitymonitoring.AqiApplication;
import org.be.airqualitymonitoring.dao.BreakpointAqiDao;
import org.be.airqualitymonitoring.entity.BreakpointAqi;
import org.be.airqualitymonitoring.error.MeasurementOutOfRangeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BreakpointAqiServiceImpl implements BreakpointAqiService {
	@Autowired
	private BreakpointAqiDao breakpointAqiDao;

	private static final Logger LOGGER = LogManager.getLogger(AqiApplication.class);

	@Override
	public BreakpointAqi saveBreakpointAQI(BreakpointAqi breakpointAqi) {
		try {
			return breakpointAqiDao.saveBreakpointAQI(breakpointAqi);
		} catch (DataAccessException e) {
			throw e;

		}
	}

	@Override
	public BreakpointAqi getBreakpointAQIById(int id) {
		try {
			return breakpointAqiDao.getBreakpointAQIById(id);
		} catch (DataAccessException e) {
			throw e;

		}

	}

	@Override
	public BreakpointAqi updateBreakpointAQI(int id, BreakpointAqi breakpointAqi) {
		try {
			return breakpointAqiDao.updateBreakpointAQI(id, breakpointAqi);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public BreakpointAqi deleteById(int id) {
		try {
			return breakpointAqiDao.deleteById(id);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public BreakpointAqi getBreakpointAQI(String pollutant, double concentration)
			throws MeasurementOutOfRangeException {

			try {
			return breakpointAqiDao.getBreakpointAQI(pollutant, concentration);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Integer getHigherAQIBreakpoint(String pollutantMin,String pollutantMax, double concentration)
			throws MeasurementOutOfRangeException {
				try {
			return breakpointAqiDao.getHigherAQIBreakpoint(pollutantMin, pollutantMax,concentration);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Integer getLowerAQIBreakpoint(String pollutantMin,String pollutantMax, double concentration) throws MeasurementOutOfRangeException {
				try {
			return breakpointAqiDao.getLowerAQIBreakpoint(pollutantMin,pollutantMax, concentration);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Double getHigherBreakpoint(String pollutantMin,String pollutantMax, double concentration) throws MeasurementOutOfRangeException {
		try {
			return breakpointAqiDao.getHigherBreakpoint(pollutantMin,pollutantMax, concentration);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public Double getLowerBreakpoint(String pollutantMin,String pollutantMax, double concentration) throws MeasurementOutOfRangeException {
		
		try {
			return breakpointAqiDao.getLowerBreakpoint(pollutantMin, pollutantMax,concentration);
		} catch (DataAccessException e) {
			throw e;
		}
	}
}
