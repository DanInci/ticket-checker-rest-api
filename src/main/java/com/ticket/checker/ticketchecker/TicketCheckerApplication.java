package com.ticket.checker.ticketchecker;

import com.ticket.checker.ticketchecker.security.SpringSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketCheckerApplication {
	
	public static final String REALM_NAME = "Ticket Checker API v1.6";

	public static void main(String[] args) {
		System.out.println(SpringSecurityConfig.encoder().encode("8C6976E5B5410415BDE908BD4DEE15DFB167A9C873FC4BB8A81F6F2AB448A918"));
		SpringApplication.run(TicketCheckerApplication.class, args);
	}

}
