package com.ticketflow.eventmanager.event.controller;

import com.ticketflow.eventmanager.event.controller.dto.CreateEventDTO;
import com.ticketflow.eventmanager.event.controller.dto.EventDTO;
import com.ticketflow.eventmanager.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDTO createEvent(@RequestBody CreateEventDTO eventDTO) {
        return eventService.createEvent(eventDTO);
    }

}
