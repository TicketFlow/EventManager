package com.ticketflow.eventmanager.event.repository;

import com.ticketflow.eventmanager.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByCategoryId(Long categoryId);

}