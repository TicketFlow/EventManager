package com.ticketflow.eventmanager.testbuilder;

import com.ticketflow.eventmanager.event.controller.dto.ArtistDTO;
import com.ticketflow.eventmanager.event.model.Artist;

public class ArtistTestBuilder {

    private static final Long ID = 1L;

    private static final String NAME = "ArtistName";

    private static final String TYPE = "Type";

    private static final String GENDER = "Gender";

    public static ArtistTestBuilder init() {
        return new ArtistTestBuilder();
    }

    public static ArtistDTO createDefaultArtistDTO() {
        return init()
                .buildDTOWithDefaultValues()
                .build();
    }

    public static Artist createDefaultArtist() {
        return init()
                .buildModelWithDefaultValues()
                .build();
    }

    public Artist.ArtistBuilder buildModelWithDefaultValues() {
        return Artist.builder()
                .id(ID)
                .name(NAME)
                .type(TYPE)
                .gender(GENDER);
    }

    public ArtistDTO.ArtistDTOBuilder buildDTOWithDefaultValues() {
        return ArtistDTO.builder()
                .id(ID)
                .name(NAME)
                .type(TYPE)
                .gender(GENDER);
    }

}
