package com.ticketflow.eventmanager.event.repository;

import com.ticketflow.eventmanager.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}