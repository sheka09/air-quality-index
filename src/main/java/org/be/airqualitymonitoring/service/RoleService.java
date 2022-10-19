package org.be.airqualitymonitoring.service;

import java.util.List;

import org.be.airqualitymonitoring.entity.Role;
import org.springframework.dao.DataAccessException;

public interface RoleService {
	public Role findById(int id) throws DataAccessException;
	
	public List<Role> findRoleByName(String name) throws DataAccessException;
	
}
