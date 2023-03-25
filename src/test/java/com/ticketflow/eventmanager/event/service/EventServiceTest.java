package com.ticketflow.eventmanager.event.service;

import com.ticketflow.eventmanager.event.controller.dto.CreateEventDTO;
import com.ticketflow.eventmanager.event.controller.dto.EventDTO;
import com.ticketflow.eventmanager.event.model.Category;
import com.ticketflow.eventmanager.event.model.Event;
import com.ticketflow.eventmanager.event.repository.EventRepository;
import com.ticketflow.eventmanager.testbuilder.CategoryTestBuilder;
import com.ticketflow.eventmanager.testbuilder.EventTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private CategoryService categoryService;

    private EventService eventService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        eventService = new EventService(eventRepository, categoryService, modelMapper);
    }

    @Test
    public void testCreateEvent() {
        Category category = CategoryTestBuilder.createDefaultCategory();
        when(categoryService.findById(category.getId())).thenReturn(category);

        Event event = EventTestBuilder.init()
                .buildModelWithDefaultValues()
                .category(category)
                .build();

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        CreateEventDTO createEventDTO = EventTestBuilder.init()
                .buildCreateDTOWithDefaultValues()
                .categoryId(category.getId())
                .build();

        EventDTO result = eventService.createEvent(createEventDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(event.getName(), result.getName());
        assertEquals(event.getDescription(), result.getDescription());
        assertEquals(event.getLocation(), result.getLocation());
        assertEquals(event.getDateTime(), result.getDateTime());
        assertEquals(event.getImagePath(), result.getImagePath());
        assertEquals(event.getCategory().getId(), result.getCategory().getId());
        assertEquals(event.getDetails(), result.getDetails());
        assertEquals(event.getArtists(), result.getArtists());
        assertEquals(event.getEventOrganizers(), result.getEventOrganizers());
        assertEquals(event.getTickets(), result.getTickets());
    }

}
