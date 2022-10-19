package org.be.airqualitymonitoring.dao;



import org.be.airqualitymonitoring.entity.BreakpointAqi;
import org.springframework.dao.DataAccessException;


public interface BreakpointAqiDao {

	public BreakpointAqi saveBreakpointAQI(BreakpointAqi breakpointAqi) throws DataAccessException;
	
	public BreakpointAqi getBreakpointAQIById(int id) throws DataAccessException ;
	
	public BreakpointAqi updateBreakpointAQI(int breakpointAQIId,BreakpointAqi breakpointAqi) throws DataAccessException;
	
	public BreakpointAqi deleteById(int breakpointId) throws DataAccessException;
	
	public BreakpointAqi getBreakpointAQI(String pollutant,double concentration);
	
	public Integer getHigherAQIBreakpoint(String pollutantMin,String PollutantMax,double concentration);
	
	public Integer getLowerAQIBreakpoint(String pollutantMin,String PollutantMax,double concentration);
	
	public Double getHigherBreakpoint(String pollutantMin,String PollutantMax,double concentration);
	
	public Double getLowerBreakpoint(String pollutantMin,String PollutantMax,double concentration);
	

	
}
