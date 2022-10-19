package org.be.airqualitymonitoring.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.be.airqualitymonitoring.AqiApplication;
import org.be.airqualitymonitoring.dao.UserDao;
import org.be.airqualitymonitoring.entity.Role;
import org.be.airqualitymonitoring.entity.User;
import org.be.airqualitymonitoring.error.UserAlreadyExistsException;
import org.be.airqualitymonitoring.error.UsernameAlreadyExistsException;
import org.be.airqualitymonitoring.twilioConfig.TwilioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twilio.Twilio;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LogManager.getLogger(AqiApplication.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private SMSService smsService;
	@Autowired
	private TwilioConfig twilioConfig;
	
	private PasswordEncoder passwordEncoder;

	@Autowired
    private AuthenticationManager authenticationManager;
	
	   @Autowired
	    public UserServiceImpl(@Lazy PasswordEncoder bcryptEncoder) {
	        this.passwordEncoder = bcryptEncoder;
	    }
	
	public boolean isValidAddress(String address) {
		// use USPS API
		return false;
	}

	@Override
	public User save(User user) throws UserAlreadyExistsException, UsernameAlreadyExistsException, DataAccessException {
		User new_user=new User();
		new_user.setUsername(user.getUsername());
		new_user.setPassword(passwordEncoder.encode(user.getPassword()));
		new_user.setEmail(user.getEmail());
		new_user.setPhoneNumber(user.getPhoneNumber());
		new_user.setStreetAddress(user.getStreetAddress());
		new_user.setCity(user.getCity());
		new_user.setState(user.getState());
		new_user.setZipCode(user.getZipCode());
		new_user.setRoles(user.getRoles());

		try {
			User userr=userDao.save(new_user);
			if(Objects.nonNull(userr)) {
				Twilio.init(twilioConfig.getAccountSid(),twilioConfig.getAuthToken());
				smsService.sendSMS(userr,"You have successfully registered.");
				
			}else {
				
			}
			return userr; 
		} catch (DataAccessException e) {
			throw e;
		} catch (UserAlreadyExistsException e) {
			throw new UserAlreadyExistsException();
		} catch (UsernameAlreadyExistsException e) {
			throw new UsernameAlreadyExistsException();
		}
	}

	@Override
	public User update(int userId, User user) throws DataAccessException {
		try {
			return userDao.update(userId, user);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public User deleteById(int userId) throws DataAccessException {
		try {
			return userDao.delete(userId);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public User findByEmail(String email) throws DataAccessException {
		try {
			return userDao.findByEmail(email);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public User findByPhonenumber(String phoneNumber) throws DataAccessException {
		try {
			return userDao.findByPhonenumber(phoneNumber);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public User findById(int userId) throws DataAccessException {
		try {
			return userDao.findById(userId);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public User findBySensor(int sensorId) throws DataAccessException {
		try {
			return userDao.findBySensor(sensorId);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public List<User> findByZipcode(String zipCode) throws DataAccessException {
		try {
			return userDao.findByZipcode(zipCode);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	@Override
	public List<User> findByRole(String role) throws DataAccessException {
		try {
			return userDao.findByRole(role);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				getAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public void login(String username, String password) throws BadCredentialsException {
		Authentication authentication = null;

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);


		} catch (BadCredentialsException e) {
			throw e;
		}
	}


}
