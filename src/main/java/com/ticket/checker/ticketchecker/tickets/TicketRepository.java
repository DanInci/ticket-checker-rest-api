package com.ticket.checker.ticketchecker.tickets;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String>{
	Page<Ticket> findByValidatedAtIsNull(Pageable pageable);
	Long countByValidatedAtIsNull();
	Page<Ticket> findByValidatedAtIsNotNull(Pageable pageable);
	Long countByValidatedAtIsNotNull();
}
