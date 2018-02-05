package com.ticket.checker.ticketchecker.users;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ticket.checker.ticketchecker.tickets.Ticket;

@Entity
@JsonFilter("UserFilter")
public class User {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Size(min=3,max=255, message="Username must have at least 3 chars and as much as 255 chars")
	private String username;
	
	private String password;
	
	@Size(max=255, message="Name must have as much as 255 chars")
	private String name;
	
	private String role;
	
	private Date createdDate;
	
	@OneToMany(mappedBy="soldBy", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Ticket> soldTickets;
	
	private int soldTicketsNo;
	
	@OneToMany(mappedBy="validatedBy", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Ticket> validatedTickets;
	
	private int validatedTicketsNo;
	
	public User() {}

	public User(String username, String password, String name, String role, Date createdDate, List<Ticket> soldTickets, List<Ticket> validatedTickets) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.role = role;
		this.createdDate = createdDate;
		this.soldTickets = soldTickets;
		this.soldTicketsNo = soldTickets.size();
		this.validatedTickets = validatedTickets;
		this.validatedTicketsNo = validatedTickets.size();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public void setSoldTickets(List<Ticket> soldTickets) {
		this.soldTickets = soldTickets;
	}
	
	public List<Ticket> getSoldTickets() {
		return soldTickets;
	}
	
	public void setSoldTicketsNo(int soldTicketsNo) {
		this.soldTicketsNo = soldTicketsNo;
	}
	
	public int getSoldTicketsNo() {
		return soldTicketsNo;
	}
	
	public void setValidatedTickets(List<Ticket> validatedTickets) {
		this.validatedTickets = validatedTickets;
	}
	
	public List<Ticket> getValidatedTickets() {
		return validatedTickets;
	}
	
	public void setValidatedTicketsNo(int validatedTicketsNo) {
		this.validatedTicketsNo = validatedTicketsNo;
	}
	
	public int getValidatedTicketsNo() {
		return validatedTicketsNo;
	}
}
