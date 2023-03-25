package com.ticketflow.eventmanager.testbuilder;

import com.ticketflow.eventmanager.event.controller.dto.CreateEventDTO;
import com.ticketflow.eventmanager.event.controller.dto.EventDTO;
import com.ticketflow.eventmanager.event.model.Event;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EventTestBuilder {

    private static final Long ID = 1L;

    private static final String NAME = "Example Name";

    private static final String DESCRIPTION = "Example Description";

    private static final LocalDateTime DATE_TIME = LocalDateTime.now();

    private static final String IMAGE_PATH = "Example Image Path";

    private static final String DETAILS = "Example Details";

    public static EventTestBuilder init() {
        return new EventTestBuilder();
    }

    public static Event createDefaultEvent() {
        return init()
                .buildModelWithDefaultValues()
                .build();
    }

    public static EventDTO createDefaultEventDTO() {
        return init()
                .buildDTOWithDefaultValues()
                .build();
    }

    public static CreateEventDTO createDefaultCreateEventDTO() {
        return init()
                .buildCreateDTOWithDefaultValues()
                .build();
    }

    public Event.EventBuilder buildModelWithDefaultValues() {
        return Event.builder()
                .id(ID)
                .name(NAME)
                .location(LocationTestBuilder.createDefaultLocation())
                .description(DESCRIPTION)
                .dateTime(DATE_TIME)
                .imagePath(IMAGE_PATH)
                .category(CategoryTestBuilder.createDefaultCategory())
                .details(DETAILS)
                .artists(Collections.singletonList(ArtistTestBuilder.createDefaultArtist()))
                .eventOrganizers(Collections.singletonList(EventOrganizerTestBuilder.createDefaultEventOrganizer()))
                .tickets(List.of("Example Ticket"));
    }

    public EventDTO.EventDTOBuilder buildDTOWithDefaultValues() {
        return EventDTO.builder()
                .id(String.valueOf(ID))
                .name(NAME)
                .location(LocationTestBuilder.createDefaultLocation())
                .description(DESCRIPTION)
                .dateTime(DATE_TIME)
                .imagePath(IMAGE_PATH)
                .category(CategoryTestBuilder.createDefaultCategoryDTO())
                .details(DETAILS)
                .artists(Collections.singletonList(ArtistTestBuilder.createDefaultArtist()))
                .eventOrganizers(Collections.singletonList(EventOrganizerTestBuilder.createDefaultEventOrganizer()))
                .tickets(List.of("Example Ticket"));
    }

    public CreateEventDTO.CreateEventDTOBuilder buildCreateDTOWithDefaultValues() {
        return CreateEventDTO.builder()
                .id(String.valueOf(ID))
                .name(NAME)
                .location(LocationTestBuilder.createDefaultLocation())
                .description(DESCRIPTION)
                .dateTime(DATE_TIME)
                .imagePath(IMAGE_PATH)
                .categoryId(CategoryTestBuilder.createDefaultCategory().getId())
                .details(DETAILS)
                .artists(Collections.singletonList(ArtistTestBuilder.createDefaultArtist().getId()))
                .eventOrganizers(Collections.singletonList(EventOrganizerTestBuilder.createDefaultEventOrganizer()))
                .tickets(List.of("Example Ticket"));
    }

}
