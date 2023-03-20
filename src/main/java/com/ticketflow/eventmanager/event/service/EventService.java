package com.ticketflow.eventmanager.event.service;

import com.ticketflow.eventmanager.event.controller.dto.EventDTO;
import com.ticketflow.eventmanager.event.model.Event;
import com.ticketflow.eventmanager.event.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Qualifier("modelMapperConfig")
    private final ModelMapper modelMapper;

    public EventService(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = toModel(eventDTO);
        Event eventSaved = eventRepository.save(event);

        return toDTO(eventSaved);
    }

    private Event toModel(EventDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }

    private EventDTO toDTO(Event event) {
        return modelMapper.map(event, EventDTO.class);
    }
}
