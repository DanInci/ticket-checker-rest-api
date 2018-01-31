package com.ticket.checker.ticketchecker.tickets;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ticket.checker.ticketchecker.users.User;

@Entity
@JsonFilter("TicketFilter")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ticket {
	
	@Id
	@Size(min=1, max=255, message="Ticked id must have at least 1 chars and as much as 255 chars")
	private String ticketId;
	
	@Size(max=255, message="Sold to must have as much as 255 chars")
	private String soldTo;
	
	@ManyToOne
	private User soldBy;
	
	private Date soldAt;
	
	@ManyToOne
	private User validatedBy;
	
	private Date validatedAt;
	
	
	public Ticket() {}

	public Ticket(String ticketId, String soldTo, User soldBy, Date soldAt, User validatedBy, Date validatedAt) {
		super();
		this.ticketId = ticketId;
		this.soldTo = soldTo;
		this.soldBy = soldBy;
		this.soldAt = soldAt;
		this.validatedBy = validatedBy;
		this.validatedAt = validatedAt;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	
	public String getSoldTo() {
		return soldTo;
	}
	
	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}
	
	public User getSoldBy() {
		return soldBy;
	}
	
	public void setSoldBy(User soldBy) {
		this.soldBy = soldBy;
	}

	public Date getSoldAt() {
		return soldAt;
	}

	public void setSoldAt(Date soldAt) {
		this.soldAt = soldAt;
	}
	
	public User getValidatedBy() {
		return validatedBy;
	}
	
	public void setValidatedBy(User validatedBy) {
		this.validatedBy = validatedBy;
	}

	public Date getValidatedAt() {
		return validatedAt;
	}

	public void setValidatedAt(Date validatedAt) {
		this.validatedAt = validatedAt;
	}

}
