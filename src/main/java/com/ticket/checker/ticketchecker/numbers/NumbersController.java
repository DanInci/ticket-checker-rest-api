package com.ticket.checker.ticketchecker.numbers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticket.checker.ticketchecker.tickets.TicketRepository;
import com.ticket.checker.ticketchecker.users.UserRepository;

@RestController
public class NumbersController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@GetMapping(path="/numbers/users")
	public Long getUsersCount(@RequestParam(value="role", required=false) String role) {
		Long usersNumbers = 0L;
		if(role == null) {
			usersNumbers = userRepository.count();
		}
		else {
			usersNumbers = userRepository.countByRole("ROLE_" + role.toUpperCase());
		}
		return usersNumbers;
	}
	
	@GetMapping(path="/numbers/tickets")
	public Long getTicketsCount(@RequestHeader(value="validated", required=false) Boolean isValidated) {
		Long ticketsNumbers;
		if(isValidated==null) {
			ticketsNumbers = ticketRepository.count();
		}
		else if(!isValidated) {
			ticketsNumbers = ticketRepository.countByValidatedAtIsNull();
		}
		else {
			ticketsNumbers = ticketRepository.countByValidatedAtIsNotNull();
		}
		return ticketsNumbers;
	}
	
	@GetMapping(path="/numbers/tickets",params="filter")
	public Long[] getAllTicketsCount(@RequestParam(value="filter", required=true) String filter) {
		Long[] numbers = {0L, 0L, 0L};
		numbers[0] = ticketRepository.count();
		if(filter.equals("all")) {
			numbers[1] = ticketRepository.countByValidatedAtIsNotNull();
			numbers[2] = numbers[0] - numbers[1];
		}
		else if(filter.equals("validated")) {
			numbers[1] = ticketRepository.countByValidatedAtIsNotNull();
		}
		else if(filter.equals("notValidated")) {
			numbers[2] = ticketRepository.countByValidatedAtIsNull();
		}
		return numbers;
	}
}
