package org.be.airqualitymonitoring.service;

import org.be.airqualitymonitoring.entity.BreakpointAqi;
import org.be.airqualitymonitoring.error.MeasurementOutOfRangeException;

public interface BreakpointAqiService {

	public BreakpointAqi saveBreakpointAQI(BreakpointAqi breakpointAqi) ;
	
	public BreakpointAqi getBreakpointAQIById(int id) ;
	
	public BreakpointAqi updateBreakpointAQI(int breakpointAQIId,BreakpointAqi breakpointAqi) ;
	
	public BreakpointAqi deleteById(int breakpointId) ;
	
	public BreakpointAqi getBreakpointAQI(String pollutant, double concentration) throws MeasurementOutOfRangeException;
	
	public Integer getHigherAQIBreakpoint(String pollutantMin,String pollutantMax,double concentration) throws MeasurementOutOfRangeException;
	
	public Integer getLowerAQIBreakpoint(String pollutantMin,String pollutantMax,double concentration) throws MeasurementOutOfRangeException;
	
	public Double getHigherBreakpoint(String pollutantMin,String pollutantMax,double concentration) throws MeasurementOutOfRangeException;
	
	public Double getLowerBreakpoint(String pollutantMin,String pollutantMax,double concentration) throws MeasurementOutOfRangeException;
	
	
	
}
