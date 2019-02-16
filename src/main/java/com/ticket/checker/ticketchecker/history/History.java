package com.ticket.checker.ticketchecker.history;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "history")
public class History {
	
	@Id
	@GeneratedValue
    @Column(name = "history_id")
    private Long historyId;

	@Column(name = "time")
	private Date time;

	@Column(name = "username")
	private String username;

	@Column(name = "request_uri")
	private String requestUri;

	@Column(name = "request_method")
	private String requestMethod;

	@Column(name = "request_status")
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
		return historyId;
	}

	public void setId(Long id) {
		this.historyId = id;
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
