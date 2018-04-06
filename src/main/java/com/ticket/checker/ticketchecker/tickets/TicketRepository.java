package com.ticket.checker.ticketchecker.tickets;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String>{
	Page<Ticket> findAllByOrderBySoldAtDesc(Pageable pageable);
	Page<Ticket> findByValidatedAtIsNullOrderBySoldAtDesc(Pageable pageable);
	Page<Ticket> findByTicketIdStartsWithIgnoreCase(String ticketId, Pageable pageable);
	Page<Ticket> findBySoldToStartsWithIgnoreCase(String soldTo, Pageable pageable);
	Long countByValidatedAtIsNull();
	Page<Ticket> findByValidatedAtIsNotNullOrderByValidatedAtDesc(Pageable pageable);
	Long countByValidatedAtIsNotNull();
	Long countBySoldAtBetween(Date startDate, Date endDate);
	Long countByValidatedAtBetween(Date startDate, Date endDate);
}
