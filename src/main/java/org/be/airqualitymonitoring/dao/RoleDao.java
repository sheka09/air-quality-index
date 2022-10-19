package org.be.airqualitymonitoring.dao;

import java.util.Collection;
import java.util.List;

import org.be.airqualitymonitoring.entity.Role;

public interface RoleDao {

	public Role findById(int id);

	public List<Role> findByName(String name);

	public Collection<Role> save(Collection<Role> roles);

}
