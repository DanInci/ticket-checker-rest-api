package com.ticket.checker.ticketchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketCheckerApplication {
	
	public static final String REALM_NAME = "Ticket Checker API v1.0";

	public static void main(String[] args) {
		SpringApplication.run(TicketCheckerApplication.class, args);
	}

}
