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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ticket.checker.ticketchecker.exceptions.NotPermittedException;
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
	public MappingJacksonValue login(@RequestHeader("Authorization") String authorization) {
		User user = util.getUserFromAuthorization(authorization);
		return setUserFilter(user);
	}
	
	@GetMapping("/users")
	public MappingJacksonValue getAllUsers() {
		List<User> users = userRepository.findAll();
		return setUserFilter(users);
	}
	
	@GetMapping(path="/users", params="role")
	public MappingJacksonValue getUsersByRole(@RequestParam("role") String role) {
		List<User> users = userRepository.findByRoleOrderByCreatedDateAsc("ROLE_" + role.toUpperCase());
		return setUserFilter(users);
	}
	
	@GetMapping("/users/{id}")
	public MappingJacksonValue getUserById(@PathVariable long id) {
		Optional<User> optional = userRepository.findById(id);
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("userId-"+id);
		}
		
		MappingJacksonValue user = setUserFilter(optional.get());
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
		user.setSoldTicketsNo(0);
		user.setValidatedTicketsNo(0);
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
		if(user.getRole().equals("ROLE_" + SpringSecurityConfig.ADMIN)) {
			throw new NotPermittedException("You can not delete an "+SpringSecurityConfig.ADMIN+" account!");
		}
		userRepository.delete(user);
	}
	
	public static MappingJacksonValue setUserFilter(Object userObject) {
		MappingJacksonValue mapping = new MappingJacksonValue(userObject);
		SimpleBeanPropertyFilter filterProperty = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","role","createdDate","soldTicketsNo","validatedTicketsNo");
		
		FilterProvider filter = new SimpleFilterProvider().addFilter("UserFilter", filterProperty);
		mapping.setFilters(filter);
		return mapping;
	}
	
}
