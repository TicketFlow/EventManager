package com.ticketflow.eventmanager.testbuilder;

import com.ticketflow.eventmanager.event.controller.dto.LocationDTO;
import com.ticketflow.eventmanager.event.model.Location;

public class LocationTestBuilder {

    private static final Long ID = 1L;

    private static final String ADDRESS = "Example Address";

    private static final String STREET_NUMBER = "123";

    private static final String REFERENCE_POINT = "Example Reference Point";

    private static final String MAPS_URL = "https://www.example.com/maps";

    private static final String NEIGHBORHOOD = "Example Neighborhood";

    private static final String CITY = "Example City";

    public static LocationTestBuilder init() {
        return new LocationTestBuilder();
    }

    public static Location createDefaultLocation() {
        return init()
                .buildModelWithDefaultValues()
                .build();
    }

    public static LocationDTO createDefaultLocationDTO() {
        return init()
                .buildDTOWithDefaultValues()
                .build();
    }

    public Location.LocationBuilder buildModelWithDefaultValues() {
        return Location.builder()
                .id(ID)
                .address(ADDRESS)
                .streetNumber(STREET_NUMBER)
                .referencePoint(REFERENCE_POINT)
                .mapsUrl(MAPS_URL)
                .neighborhood(NEIGHBORHOOD)
                .city(CITY);
    }

    public LocationDTO.LocationDTOBuilder buildDTOWithDefaultValues() {
        return LocationDTO.builder()
                .id(ID)
                .address(ADDRESS)
                .streetNumber(STREET_NUMBER)
                .referencePoint(REFERENCE_POINT)
                .mapsUrl(MAPS_URL)
                .neighborhood(NEIGHBORHOOD)
                .city(CITY);
    }


}
