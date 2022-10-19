package org.be.airqualitymonitoring.service;

import java.util.List;

import javax.transaction.Transactional;

import org.be.airqualitymonitoring.dao.RoleDao;
import org.be.airqualitymonitoring.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{
    @Autowired
	private RoleDao roleDao;
	@Override
	public Role findById(int id) throws DataAccessException{
		try {
			return roleDao.findById(id);
		} catch (DataAccessException e) {
			throw e;
		}
	}
	@Override
	public List<Role> findRoleByName(String name) throws DataAccessException {
		try {
			return roleDao.findByName(name);
		} catch (DataAccessException e) {
			throw e;
		}
	}

}
