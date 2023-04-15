package com.ticketflow.eventmanager.event.controller.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFilter {

    private Long id;

    private String name;

    private String description;

    private String ageGroup;

    private String owner;

}
