package com.ticketflow.eventmanager.testbuilder;

import com.ticketflow.eventmanager.event.controller.dto.EventTypeDTO;
import com.ticketflow.eventmanager.event.model.EventType;

public class EventTypeTestBuilder {

    private static final Long ID = 1L;

    private static final String NAME = "Example Name";

    private static final String DESCRIPTION = "Example Description";

    public static EventTypeTestBuilder init() {
        return new EventTypeTestBuilder();
    }

    public static EventType createDefaultEventType() {
        return init()
                .buildModelWithDefaultValues()
                .build();
    }

    public static EventTypeDTO createDefaultEventTypeDTO() {
        return init()
                .buildDTOWithDefaultValues()
                .build();
    }

    public EventType.EventTypeBuilder buildModelWithDefaultValues() {
        return EventType.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION);
    }

    public EventTypeDTO.EventTypeDTOBuilder buildDTOWithDefaultValues() {
        return EventTypeDTO.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION);
    }


}
