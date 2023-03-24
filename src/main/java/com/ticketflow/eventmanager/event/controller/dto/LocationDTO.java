package com.ticketflow.eventmanager.event.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {

    private Long id;

    private String address;

    private String streetNumber;

    private String referencePoint;

    private String mapsUrl;

    private String neighborhood;

    private String city;
}
