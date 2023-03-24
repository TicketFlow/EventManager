package com.ticketflow.eventmanager.event.controller.dto;

import com.ticketflow.eventmanager.event.model.Artist;
import com.ticketflow.eventmanager.event.model.EventOrganizer;
import com.ticketflow.eventmanager.event.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {

    private String id;

    private String name;

    private Location location;

    private String description;

    private LocalDateTime dateTime;

    private String imagePath;

    private Long categoryId;

    private String details;

    private List<Long> artists;

    private List<EventOrganizer> eventOrganizers;

    private List<String> tickets;
}
