package com.ticket.checker.ticketchecker.tickets;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long>{
	List<Ticket> findByValidatedAtIsNull();
	List<Ticket> findByValidatedAtIsNotNull();
}
