package org.be.airqualitymonitoring.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.be.airqualitymonitoring.entity.User;
import org.be.airqualitymonitoring.error.UserAlreadyExistsException;
import org.be.airqualitymonitoring.error.UserNotFoundException;
import org.be.airqualitymonitoring.error.UsernameAlreadyExistsException;
import org.be.airqualitymonitoring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> saveUser(@RequestBody User user)
			throws UserAlreadyExistsException, UsernameAlreadyExistsException {
		try {
			return new ResponseEntity<User>(userService.save(user), HttpStatus.CREATED);
		} catch (UserAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (UsernameAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);

		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@PathVariable int id, User user) {
		try {
			return new ResponseEntity<User>(userService.update(id, user), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> findBUseryId(@PathVariable int id) throws UserNotFoundException {
		return new ResponseEntity<User>(userService.findById(id), HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable int id) throws UserNotFoundException {
		try {
			return new ResponseEntity<User>(userService.deleteById(id), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("/users/email/{email}")
	public ResponseEntity<?> findUserByEmail(@PathVariable String email) {
		try {
			return new ResponseEntity<User>(userService.findByEmail(email), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/users/phone/{phoneNumber}")
	public ResponseEntity<?> findUserByPhonenumber(@PathVariable String phoneNumber) {
		try {
			return new ResponseEntity<User>(userService.findByPhonenumber(phoneNumber), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/users/sensor/{id}")
	public ResponseEntity<?> findUserBySensor(@PathVariable int id) {
		try {
			return new ResponseEntity<User>(userService.findBySensor(id), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/users/zipcode/{zipCode}")
	public ResponseEntity<?> findUsersByZipcode(@PathVariable String zipCode) {
		try {
			return new ResponseEntity<List<User>>(userService.findByZipcode(zipCode), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/users/role/{role}")
	public ResponseEntity<?> findUsersByRole(@PathVariable String role) {
		try {
			return new ResponseEntity<List<User>>(userService.findByRole(role), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody String username, @RequestBody String password) {
			try {
			userService.login(username,password);
			System.out.println("You have successfully logged in.");
		} catch (BadCredentialsException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		
	
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	 

}
