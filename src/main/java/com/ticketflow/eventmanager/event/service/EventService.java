package com.ticketflow.eventmanager.event.service;

import com.ticketflow.eventmanager.event.controller.dto.CreateEventDTO;
import com.ticketflow.eventmanager.event.controller.dto.EventDTO;
import com.ticketflow.eventmanager.event.model.Artist;
import com.ticketflow.eventmanager.event.model.Category;
import com.ticketflow.eventmanager.event.model.Event;
import com.ticketflow.eventmanager.event.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    private final CategoryService categoryService;

    private final ArtistService artistService;

    @Qualifier("modelMapperConfig")
    private final ModelMapper modelMapper;

    public EventService(EventRepository eventRepository, CategoryService categoryService, ArtistService artistService,
                        ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.categoryService = categoryService;
        this.artistService = artistService;
        this.modelMapper = modelMapper;
    }

    public EventDTO createEvent(CreateEventDTO eventDTO) {
        Event event = toModel(eventDTO);
        assignCategoryToEvent(eventDTO, event);
        assignArtistsToEvent(eventDTO, event);

        Event savedEvent = eventRepository.save(event);

        return toDTO(savedEvent);
    }

    private void assignCategoryToEvent(CreateEventDTO eventDTO, Event event) {
        Category category = categoryService.findById(eventDTO.getCategoryId());
        event.setCategory(category);
    }

    private void assignArtistsToEvent(CreateEventDTO eventDTO, Event event) {
        List<Artist> artists = artistService.findByIds(eventDTO.getArtists());
        event.setArtists(artists);
    }

    private Event toModel(CreateEventDTO eventDTO) {
        return modelMapper.map(eventDTO, Event.class);
    }

    private EventDTO toDTO(Event event) {
        return modelMapper.map(event, EventDTO.class);
    }
}
