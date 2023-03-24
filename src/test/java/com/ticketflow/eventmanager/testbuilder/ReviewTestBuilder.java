package com.ticketflow.eventmanager.testbuilder;

import com.ticketflow.eventmanager.event.controller.dto.ReviewDTO;
import com.ticketflow.eventmanager.event.model.Review;

import java.time.LocalDateTime;

public class ReviewTestBuilder {

    private static final Long ID = 1L;

    private static final String EVENT_ID = "example_event_id";

    private static final String USER_ID = "example_user_id";

    private static final Float RATING = 4.5f;

    private static final String COMMENT = "Example comment";

    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();

    public static ReviewTestBuilder init() {
        return new ReviewTestBuilder();
    }

    public static Review createDefaultReview() {
        return init()
                .buildModelWithDefaultValues()
                .build();
    }

    public static ReviewDTO createDefaultReviewDTO() {
        return init()
                .buildDTOWithDefaultValues()
                .build();
    }

    public Review.ReviewBuilder buildModelWithDefaultValues() {
        return Review.builder()
                .id(ID)
                .eventId(EVENT_ID)
                .userId(USER_ID)
                .rating(RATING)
                .comment(COMMENT)
                .timestamp(TIMESTAMP);
    }

    public ReviewDTO.ReviewDTOBuilder buildDTOWithDefaultValues() {
        return ReviewDTO.builder()
                .id(ID)
                .eventId(EVENT_ID)
                .userId(USER_ID)
                .rating(RATING)
                .comment(COMMENT)
                .timestamp(TIMESTAMP);
    }

}
