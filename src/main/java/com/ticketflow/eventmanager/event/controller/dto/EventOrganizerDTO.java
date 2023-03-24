package com.ticketflow.eventmanager.event.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOrganizerDTO {

    private Long id;

    private String userId;

    private String name;

    private String title;
}
