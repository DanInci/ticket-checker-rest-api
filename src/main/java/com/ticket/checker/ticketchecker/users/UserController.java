package com.ticket.checker.ticketchecker.users;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.ticket.checker.ticketchecker.exceptions.ResourceNotFoundException;
import com.ticket.checker.ticketchecker.exceptions.UsernameExistsException;
import com.ticket.checker.ticketchecker.security.SpringSecurityConfig;
import com.ticket.checker.ticketchecker.tickets.Ticket;
import com.ticket.checker.ticketchecker.tickets.TicketController;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired UserUtil util;
	
	@GetMapping(path="/")
	public ResponseEntity<Object> status() {
		return new ResponseEntity<Object>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/login")
	public User login(@RequestHeader("Authorization") String authorization) {
		return util.getUserFromAuthorization(authorization);
	}
	
	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping("/users/{id}")
	public User getAllUsers(@PathVariable long id) {
		Optional<User> optional = userRepository.findById(id);
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("userId-"+id);
		}
		
		User user = optional.get();
		return user;
	}
	
	@GetMapping("/users/{id}/validated")
	public MappingJacksonValue getValidatedTicketsByUserId(@PathVariable long id) {
		Optional<User> optional = userRepository.findById(id);
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("userId-"+id);
		}
		
		User user = optional.get();
		List<Ticket> validatedTickets = user.getValidatedTickets();
		MappingJacksonValue map = TicketController.setTicketFilters(validatedTickets, true);
		return map;
	}
	
	@GetMapping("/users/{id}/sold")
	public MappingJacksonValue getSoldTicketsByUserId(@PathVariable long id) {
		Optional<User> optional = userRepository.findById(id);
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("userId-"+id);
		}
		
		User user = optional.get();
		List<Ticket> soldTickets = user.getSoldTickets();
		MappingJacksonValue map = TicketController.setTicketFilters(soldTickets, true);
		return map;
	}
	
	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		String username = user.getUsername();
		String password = user.getPassword();
		Optional<User> usersWithSameUsername = userRepository.findByUsername(username);
		if(usersWithSameUsername.isPresent()) {
			throw new UsernameExistsException("username-"+username);
		}
		
		user.setCreatedDate(new Date());
		
		String hashedUserPassword = SpringSecurityConfig.encoder().encode(password);
		user.setPassword(hashedUserPassword);
		userRepository.save(user);
		
		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if(!optionalUser.isPresent()) {
			throw new ResourceNotFoundException("User by usedId: " + id);
		}
		
		User user = optionalUser.get();
		userRepository.delete(user);
	}
	
}
