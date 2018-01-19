package com.ticket.checker.ticketchecker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TicketValidatedException extends RuntimeException {

	public TicketValidatedException(String message) {
		super(message);
	}
}
