package com.ticket.checker.ticketchecker.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticket.checker.ticketchecker.history.History;

public interface HistoryRepository extends JpaRepository<History, Long> {

}
