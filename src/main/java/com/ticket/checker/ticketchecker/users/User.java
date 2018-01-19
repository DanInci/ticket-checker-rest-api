package com.ticket.checker.ticketchecker.users;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ticket.checker.ticketchecker.tickets.Ticket;

@Entity
public class User {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	private String username;
	@JsonIgnore
	private String password;
	
	private String name;
	private String role;
	private Date createdDate;
	
	@OneToMany(mappedBy="soldBy", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Ticket> soldTickets;
	
	@OneToMany(mappedBy="validatedBy", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Ticket> validatedTickets;
	
	public User() {}

	public User(String username, String password, String name, String role, Date createdDate, List<Ticket> soldTickets, List<Ticket> validatedTickets) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.role = role;
		this.createdDate = createdDate;
		this.soldTickets = soldTickets;
		this.validatedTickets = validatedTickets;
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
	
	public void setValidatedTickets(List<Ticket> validatedTickets) {
		this.validatedTickets = validatedTickets;
	}
	
	public List<Ticket> getValidatedTickets() {
		return validatedTickets;
	}
}
