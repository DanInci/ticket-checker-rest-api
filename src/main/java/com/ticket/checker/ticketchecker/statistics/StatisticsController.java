package com.ticket.checker.ticketchecker.statistics;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticket.checker.ticketchecker.tickets.TicketRepository;
import com.ticket.checker.ticketchecker.users.UserRepository;

@RestController
public class StatisticsController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@GetMapping(path="/statistics/numbers/users")
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
	
	@GetMapping(path="/statistics/numbers/tickets")
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
	
	@GetMapping(path="/statistics/numbers/tickets",params="filter")
	public Long[] getAllTicketsCount(@RequestParam(value="filter", required=true) String filter) {
		Long[] numbers = {0L, 0L};
		if(filter.equals("all")) {
			numbers[0] = ticketRepository.count();
			numbers[1] = ticketRepository.countByValidatedAtIsNotNull();
		}
		else if(filter.equals("validated")) {
			numbers[0] = ticketRepository.countByValidatedAtIsNotNull();
		}
		else if(filter.equals("notValidated")) {
			numbers[0] = ticketRepository.countByValidatedAtIsNull();
		}
		return numbers;
	}
	
	@GetMapping(path="/statistics/tickets")
	public List<Statistic> getTicketsCount(@RequestParam(value="type", required=true) String type, @RequestParam(value="interval", required=true) String interval, 
			@RequestParam(value="size", required=false) Integer size) {
		size = (size == null || size > 15) ? 7 : size;
		List<Statistic> statistics = new ArrayList<Statistic>();
		if(interval.toUpperCase().equals("HOURLY")) {
			statistics = getTicketsStatisticsForInterval(3600000L, type, size);
		}
		else if(interval.toUpperCase().equals("DAILY")) {
			statistics = getTicketsStatisticsForInterval(86400000L, type, size);
		}
		else if(interval.toUpperCase().equals("WEEKLY")) {
			statistics = getTicketsStatisticsForInterval(604800000L, type, size);
		}
		return statistics;
	}
	
	private List<Statistic> getTicketsStatisticsForInterval(Long interval, String type, int size) {
		List<Statistic> statistics = new ArrayList<Statistic>();
		Date beginDate = null;
		if(interval != null && interval == 604800000L) {
			LocalDate nowLocale = LocalDate.now();
			LocalDate localDate = nowLocale.with(DayOfWeek.MONDAY);
			beginDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		else {
			Long now = System.currentTimeMillis();
			Long roundedTime = now - (now % interval);
			beginDate = new Date(roundedTime);
		}
		
		Long startTime = beginDate.getTime() - size * interval;
		Long endTime = startTime + interval;
		for(int i=0;i<size;i++) {
			startTime += interval;
			endTime += interval;
			if(type.toUpperCase().equals("VALIDATED")) {
				Date startDate = new Date(startTime);
				Date endDate = new Date(endTime);
				Long ticketsCount = ticketRepository.countByValidatedAtBetween(startDate, endDate);
				statistics.add(new Statistic(startDate, ticketsCount ));
			}
			else if(type.toUpperCase().equals("SOLD")) {
				Date startDate = new Date(startTime);
				Date endDate = new Date(endTime);
				Long ticketsCount = ticketRepository.countBySoldAtBetween(startDate, endDate);
				statistics.add(new Statistic(startDate, ticketsCount));
			}
		}
		return statistics;
	}
 }
