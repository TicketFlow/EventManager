package com.ticketflow.eventmanager.event.service;

import com.ticketflow.eventmanager.event.controller.dto.CreateEventDTO;
import com.ticketflow.eventmanager.event.model.Category;
import com.ticketflow.eventmanager.event.model.Event;
import com.ticketflow.eventmanager.event.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;

    private final CategoryService categoryService;

    @Qualifier("modelMapperConfig")
    private final ModelMapper modelMapper;

    public EventService(EventRepository eventRepository, CategoryService categoryService, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    public CreateEventDTO createEvent(CreateEventDTO eventDTO) {
        Category category = categoryService.findById(eventDTO.getCategoryId());

        Event event = toModel(eventDTO);
        event.setCategory(category);
        Event eventSaved = eventRepository.save(event);

        return toDTO(eventSaved);
    }

    private Event toModel(CreateEventDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }

    private CreateEventDTO toDTO(Event event) {
        return modelMapper.map(event, CreateEventDTO.class);
    }
}
