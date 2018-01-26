package com.ticket.checker.ticketchecker.history;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class History {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Date time;
	
	private String username;
	
	private String requestUri;
	
	private String requestMethod;
	
	private int responseStatus;
	
	public History() {}

	public History(Date time, String username, String requestURI, String requestMethod, int responseStatus) {
		super();
		this.time = time;
		this.username = username;
		this.requestUri = requestURI;
		this.requestMethod = requestMethod;
		this.responseStatus = responseStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRequestURI() {
		return requestUri;
	}

	public void setRequestURI(String requestURI) {
		this.requestUri = requestURI;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public int getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(int responseStatus) {
		this.responseStatus = responseStatus;
	}
	
}
