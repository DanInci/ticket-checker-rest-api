package com.ticket.checker.ticketchecker.tickets;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String>{
	List<Ticket> findByValidatedAtIsNullOrderBySoldAtAsc();
	List<Ticket> findByValidatedAtIsNotNullOrderByValidatedAtAsc();
}
