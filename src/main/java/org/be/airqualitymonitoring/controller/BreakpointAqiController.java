package org.be.airqualitymonitoring.controller;

import org.be.airqualitymonitoring.entity.BreakpointAqi;
import org.be.airqualitymonitoring.service.BreakpointAqiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BreakpointAqiController {
	@Autowired
	private BreakpointAqiService breakpointAqiService;
	
	
	@PostMapping("/breakpointAQI")
	public ResponseEntity<?> saveBreakpointAQI(@RequestBody BreakpointAqi breakpointAqi) {
		try {
			return new ResponseEntity<BreakpointAqi>(breakpointAqiService.saveBreakpointAQI(breakpointAqi),HttpStatus.CREATED);

		}catch(DataAccessException e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PutMapping("/breakpointAQI/{id}")
	public ResponseEntity<?> updateBreakpointAQI(@PathVariable int id,@RequestBody BreakpointAqi breakpointAqi) {
		try {
	   return new ResponseEntity<BreakpointAqi>(breakpointAqiService.updateBreakpointAQI(id, breakpointAqi),HttpStatus.OK);}
		catch(DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/breakpointAQI/{id}")
	public ResponseEntity<?> deleteBreakpointAQI(@PathVariable int id) {

		try {
			return new ResponseEntity<BreakpointAqi>(breakpointAqiService.deleteById(id), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}


	}
	
	@GetMapping("/breakpointAQI/{id}")
	public ResponseEntity<?> getBreakpointAQIById(@PathVariable int id){
		try {
		return new ResponseEntity<BreakpointAqi>(breakpointAqiService.getBreakpointAQIById(id),HttpStatus.OK);}
		catch(DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	

}
