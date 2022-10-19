package org.be.airqualitymonitoring.service;

import java.util.List;

import org.be.airqualitymonitoring.entity.User;
import org.be.airqualitymonitoring.error.UserAlreadyExistsException;
import org.be.airqualitymonitoring.error.UsernameAlreadyExistsException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  extends UserDetailsService {

	public User save(User user) throws UserAlreadyExistsException, UsernameAlreadyExistsException;

	public User update(int userId, User user) ;

	public User deleteById(int userId) ;

	public User findByEmail(String email);

	public User findByPhonenumber(String phoneNumber);

	public User findById(int userId);

	public User findBySensor(int sensorId);

	public List<User> findByZipcode(String zipCode);
	
	public List<User> findByRole(String role);
	
	public void login(String username,String password) throws BadCredentialsException;
}
