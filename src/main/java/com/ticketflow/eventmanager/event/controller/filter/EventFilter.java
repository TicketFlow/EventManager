package com.ticketflow.eventmanager.event.controller.filter;

import com.ticketflow.eventmanager.event.controller.dto.CategoryDTO;
import com.ticketflow.eventmanager.event.model.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFilter {
    private String id;
    private String name;
    private String city;
    private CategoryDTO category;
    private List<Artist> artists;
}
