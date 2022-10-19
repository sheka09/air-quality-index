package org.be.airqualitymonitoring.dao;

import java.util.List;

import org.be.airqualitymonitoring.entity.User;
import org.be.airqualitymonitoring.error.UserAlreadyExistsException;
import org.be.airqualitymonitoring.error.UsernameAlreadyExistsException;
import org.springframework.dao.DataAccessException;


public interface UserDao {

	public User save(User user) throws UserAlreadyExistsException, UsernameAlreadyExistsException,DataAccessException;

	public User update(int userId, User user) throws DataAccessException;

	public User delete(int userId) throws DataAccessException;

	public User findByEmail(String email) throws DataAccessException;
	
	public User findByUsername(String username) throws DataAccessException;

	public User findByPhonenumber(String phoneNumber) throws DataAccessException;

	public User findById(int userId) throws DataAccessException;

	public User findBySensor(int sensorId) throws DataAccessException;

	public List<User> findByZipcode(String zipCode) throws DataAccessException;
	
	public List<User> findByRole(String role) throws DataAccessException;

}
