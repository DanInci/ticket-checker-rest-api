package com.ticket.checker.ticketchecker.tickets;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ticket.checker.ticketchecker.exceptions.ResourceNotFoundException;
import com.ticket.checker.ticketchecker.exceptions.TicketExistsException;
import com.ticket.checker.ticketchecker.exceptions.TicketValidatedException;
import com.ticket.checker.ticketchecker.users.User;
import com.ticket.checker.ticketchecker.users.UserUtil;

@RestController
public class TicketController {

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private UserUtil userUtil;
	
	@GetMapping(path="/tickets")
	public MappingJacksonValue getAllTickets() {
		List<Ticket> ticketList = ticketRepository.findAll();
		MappingJacksonValue map = setTicketFilters(ticketList, true);
		return map;
	}
	
	
	@GetMapping(path="/tickets",params="validated")
	public MappingJacksonValue getTicketsByValidation(@RequestParam("validated") boolean isValidated) {
		List<Ticket> ticketList;
		if(isValidated) {
			ticketList = ticketRepository.findByValidatedAtIsNotNull();
		}
		else {
			ticketList = ticketRepository.findByValidatedAtIsNull();
		}
		MappingJacksonValue map = setTicketFilters(ticketList, true);
		return map;
	}
	
	@GetMapping(path="/tickets",params="validate") 
	public void validateTicketById(@RequestHeader("Authorization") String authorization, @RequestParam("validate") String ticketId) {
		User validatedBy = userUtil.getUserFromAuthorization(authorization);
		
		Optional<Ticket> optional = ticketRepository.findById(ticketId);
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("Ticket " + ticketId + " was not found!");
		}
		Ticket ticket = optional.get();
		if(ticket.getValidatedAt() != null) {
			throw new TicketValidatedException("Ticket "+ ticketId +" was already validated!");
		}
		ticket.setValidatedBy(validatedBy);
		ticket.setValidatedAt(new Date());
		ticketRepository.save(ticket);
		
		userUtil.incrementUserValidatedTickets(validatedBy);
	}
	
	@GetMapping("/tickets/{ticketId}")
	public MappingJacksonValue getTicketById(@PathVariable String ticketId) {
		Optional<Ticket> optional = ticketRepository.findById(ticketId);
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("Ticket " + ticketId + " was not found!");
		}
		Ticket ticket = optional.get();
		MappingJacksonValue map = setTicketFilters(ticket, false);
		return map;
	}
	
	@PostMapping("/tickets")
	public void createTicket(@RequestHeader("Authorization") String authorization, @RequestBody Ticket ticket) {
		User soldBy = userUtil.getUserFromAuthorization(authorization);
		
		String ticketId = ticket.getTicketId();
		Optional<Ticket> optional = ticketRepository.findById(ticketId);
		if(optional.isPresent()) {
			throw new TicketExistsException("Ticket " + ticketId + " already exists!");
		}
		ticket.setSoldAt(new Date());
		ticket.setSoldBy(soldBy);

		ticketRepository.save(ticket);
		userUtil.incrementUserSoldTickets(soldBy);
	}
	
	@DeleteMapping(path="/tickets/{ticketId}")
	public void deleteTicketById(@PathVariable String ticketId) {
		Optional<Ticket> optional = ticketRepository.findById(ticketId);
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("Ticket " + ticketId + " doesnt not exist!");
		}
		ticketRepository.deleteById(ticketId);
	}
	
	@GetMapping(path="/tickets/numbers")
	public Long getAllTicketNumbers() {
		return ticketRepository.count();
	}
	
	@GetMapping(path="/tickets/numbers",params="validated")
	public Integer getTicketCount(@RequestParam("validated") boolean isValidated) {
		if(isValidated) {
			List<Ticket> validatedTickets = ticketRepository.findByValidatedAtIsNotNull();
			return validatedTickets.size();
		}
		else {
			List<Ticket> notValidatedTickets = ticketRepository.findByValidatedAtIsNull();
			return notValidatedTickets.size();
		}
	}
	
	public static MappingJacksonValue setTicketFilters(Object ticketObject, boolean hideUserDetails) {
		MappingJacksonValue mapping = new MappingJacksonValue(ticketObject);
		SimpleBeanPropertyFilter filterProperty;
		if(hideUserDetails) {
			filterProperty = SimpleBeanPropertyFilter.filterOutAllExcept("ticketId","soldTo","soldAt","validatedAt");
		} 
		else {
			filterProperty = SimpleBeanPropertyFilter.serializeAll();
		}
		
		FilterProvider filter = new SimpleFilterProvider().addFilter("TicketFilter", filterProperty);
		mapping.setFilters(filter);
		return mapping;
	}
	
}
