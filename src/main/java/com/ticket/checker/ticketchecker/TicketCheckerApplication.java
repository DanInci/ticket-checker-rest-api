package com.ticket.checker.ticketchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TicketCheckerApplication {
	
	public static final String REALM_NAME = "Ticket Checker API v1.0";

	public static void main(String[] args) {
		SpringApplication.run(TicketCheckerApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("admin"));
		System.out.println(new BCryptPasswordEncoder().encode("test"));
	}

}
