package com.ticket.checker.ticketchecker.tickets;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ticket.checker.ticketchecker.exceptions.ResourceNotFoundException;
import com.ticket.checker.ticketchecker.exceptions.TicketExistsException;
import com.ticket.checker.ticketchecker.exceptions.TicketValidationException;
import com.ticket.checker.ticketchecker.exceptions.UnauthorizedRequestException;
import com.ticket.checker.ticketchecker.security.SpringSecurityConfig;
import com.ticket.checker.ticketchecker.users.User;
import com.ticket.checker.ticketchecker.users.UserController;
import com.ticket.checker.ticketchecker.users.UserUtil;

@RestController
public class TicketController {

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private UserUtil userUtil;
	
	@GetMapping(path="/tickets")
	public MappingJacksonValue getTickets(@RequestParam(value="validated", required=false) Boolean isValidated, Pageable pageable) {
		Page<Ticket> ticketPagingList = null;
		if(isValidated != null) {
			if(!isValidated) {
				ticketPagingList = ticketRepository.findByValidatedAtIsNull(pageable);
			}
			else {
				ticketPagingList = ticketRepository.findByValidatedAtIsNotNull(pageable);
			}
		}
		else {
			ticketPagingList = ticketRepository.findAll(pageable);
		}
		List<Ticket> ticketList = ticketPagingList.getContent();
		MappingJacksonValue map = setTicketFilters(ticketList, true);
		return map;
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
	public void createTicket(@RequestHeader("Authorization") String authorization,@Valid @RequestBody Ticket ticket) {
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
	
	@PostMapping(path="/tickets/{ticketId}") 
	public void validateTicketById(@RequestHeader("Authorization") String authorization, @RequestHeader("validate") Boolean validate, @PathVariable String ticketId) {
		User userMakingRequest = userUtil.getUserFromAuthorization(authorization);
		
		Optional<Ticket> optional = ticketRepository.findById(ticketId);
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("Ticket " + ticketId + " was not found!");
		}
		Ticket ticket = optional.get();
		if(validate) {
			if(ticket.getValidatedAt() != null) {
				throw new TicketValidationException("Ticket "+ ticketId +" was already validated!");
			}
			userUtil.incrementUserValidatedTickets(userMakingRequest);
			ticket.setValidatedBy(userMakingRequest);
			ticket.setValidatedAt(new Date());
		}
		else {
			if(userMakingRequest.getRole().equals("ROLE_" + SpringSecurityConfig.ADMIN)) {
				if(ticket.getValidatedAt() == null) {
					throw new TicketValidationException("Ticket "+ ticketId +" is not validated!");
				}
				User userThatValidatedTicket = ticket.getValidatedBy();
				userUtil.decrementUserValidatedTickets(userThatValidatedTicket);
				ticket.setValidatedBy(null);
				ticket.setValidatedAt(null);
			}
			else {
				throw new UnauthorizedRequestException("You are not authorized to invalidate tickets!");
			}
		}
		ticketRepository.save(ticket);
	}
	
	@DeleteMapping(path="/tickets/{ticketId}")
	public void deleteTicketById(@PathVariable String ticketId) {
		Optional<Ticket> optional = ticketRepository.findById(ticketId);
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("Ticket " + ticketId + " doesnt not exist!");
		}
		Ticket ticket = optional.get();
		User userThatSoldTheTicket = ticket.getSoldBy();
		userUtil.decrementUserSoldTickets(userThatSoldTheTicket);
		ticketRepository.delete(ticket);
	}
	
	public static MappingJacksonValue setTicketFilters(Object ticketObject, boolean hideUserDetails) {
		MappingJacksonValue mapping = new MappingJacksonValue(ticketObject);
		SimpleFilterProvider ticketFilter = new SimpleFilterProvider().addFilter("TicketFilter", getTicketFilterProperty(hideUserDetails));
		if(!hideUserDetails) {
			ticketFilter.addFilter("UserFilter", UserController.getUserFilterProperty());
		}
		mapping.setFilters(ticketFilter);
		return mapping;
	}
	
	public static SimpleBeanPropertyFilter getTicketFilterProperty(boolean hideUserDetails) {
		if(hideUserDetails) {
			return SimpleBeanPropertyFilter.filterOutAllExcept("ticketId","soldTo","soldAt","validatedAt");
		}
		else {
			return SimpleBeanPropertyFilter.serializeAll();
		}
	}
	
}
