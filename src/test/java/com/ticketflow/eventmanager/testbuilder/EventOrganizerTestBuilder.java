package com.ticketflow.eventmanager.testbuilder;

import com.ticketflow.eventmanager.event.controller.dto.EventOrganizerDTO;
import com.ticketflow.eventmanager.event.model.EventOrganizer;

public class EventOrganizerTestBuilder {

    private static final Long ID = 1L;

    private static final String USER_ID = "example_user_id";

    private static final String NAME = "Example Name";

    private static final String TITLE = "Example Title";

    public static EventOrganizerTestBuilder init() {
        return new EventOrganizerTestBuilder();
    }

    public static EventOrganizer createDefaultEventOrganizer() {
        return init()
                .buildModelWithDefaultValues()
                .build();
    }

    public static EventOrganizerDTO createDefaultEventOrganizerDTO() {
        return init()
                .buildDTOWithDefaultValues()
                .build();
    }

    public EventOrganizer.EventOrganizerBuilder buildModelWithDefaultValues() {
        return EventOrganizer.builder()
                .id(ID)
                .userId(USER_ID)
                .name(NAME)
                .title(TITLE);
    }

    public EventOrganizerDTO.EventOrganizerDTOBuilder buildDTOWithDefaultValues() {
        return EventOrganizerDTO.builder()
                .id(ID)
                .userId(USER_ID)
                .name(NAME)
                .title(TITLE);
    }

}
