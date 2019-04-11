package com.ticket.checker.ticketchecker.statistics;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
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
	public Long getUsersCount(@RequestParam(value="type", required=false) String type, @RequestParam(value="value", required=false) String value) {
		Long usersNumbers = 0L;
		if(type != null && value != null) {
			switch(type.toUpperCase()) {
				case "ROLE": {
					usersNumbers = userRepository.countByRole("ROLE_" + value.toUpperCase());
					break;
				}
				case "SEARCH": {
					usersNumbers = userRepository.countByNameStartsWithIgnoreCase(value);
					break;
				}
			}
		}
		else {
			usersNumbers = userRepository.count();
		}
		return usersNumbers;
	}
	
	@GetMapping(path="/statistics/numbers/tickets") 
	public Long getTicketsCount(@RequestParam(value="type", required=false) String type, @RequestParam(value="value", required=false) String value) {
		Long ticketNumbers = 0L;
		if(type!=null && value != null) {
			switch(type.toUpperCase()) {
				case "VALIDATED": {
					if(value.toUpperCase().equals("TRUE")) {
						ticketNumbers = ticketRepository.countByValidatedAtIsNotNull();
					}
					else {
						ticketNumbers = ticketRepository.countByValidatedAtIsNull();
					}
					break;
				}
				case "SEARCH": {
					ticketNumbers = ticketRepository.countByTicketIdStartsWithIgnoreCaseOrSoldToStartsWithIgnoreCase(value, value);
					break;
				}
			}
		}
		else {
			ticketNumbers = ticketRepository.count();
		}
		return ticketNumbers;
	}
	
	@GetMapping(path="/statistics/tickets")
	public List<Statistic> getTicketsCount(@RequestParam(value="type", required=true) String type, @RequestParam(value="interval", required=true) String interval,
										   @RequestParam(value="size", required=false) Integer size, @RequestParam(value="from", required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date from) {
		size = (size == null || size > 24) ? 7 : size;
		List<Statistic> statistics = new ArrayList<Statistic>();
		if(interval.toUpperCase().equals("HOURLY")) {
			statistics = getTicketsStatisticsForInterval(3600000L, type, size, from);
		}
		else if(interval.toUpperCase().equals("DAILY")) {
			statistics = getTicketsStatisticsForInterval(86400000L, type, size, from);
		}
		else if(interval.toUpperCase().equals("WEEKLY")) {
			statistics = getTicketsStatisticsForInterval(604800000L, type, size, from);
		}
		return statistics;
	}
	
	private List<Statistic> getTicketsStatisticsForInterval(Long interval, String type, int size, Date from) {
		List<Statistic> statistics = new ArrayList<Statistic>();
		Date beginDate = null;
		if(from != null) {
			beginDate = from;
		}
		else {
			if(interval == 604800000L) {
				LocalDate nowLocale = LocalDate.now();
				LocalDate localDate = nowLocale.with(DayOfWeek.MONDAY);
				beginDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			}
			else {
				Long now = System.currentTimeMillis();
				Long roundedTime = now - (now % interval);
				beginDate = new Date(roundedTime);
			}
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
