package com.ticket.checker.ticketchecker.statistics;

import java.util.Date;

public class Statistic {
	private Date date;
	private Long count;
	
	public Statistic() {}
	
	public Statistic(Date date, Long count) {
		this.date = date;
		this.count = count;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
}
