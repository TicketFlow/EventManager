package com.ticketflow.eventmanager.event.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;

    private String eventId;

    private String userId;

    private Float rating;

    private String comment;

    private LocalDateTime timestamp;

}
