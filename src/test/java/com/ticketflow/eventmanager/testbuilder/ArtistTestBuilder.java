package com.ticketflow.eventmanager.testbuilder;

import com.ticketflow.eventmanager.event.controller.dto.ArtistDTO;
import com.ticketflow.eventmanager.event.model.Artist;

public class ArtistTestBuilder {

    private static final Long ID = 1L;

    private static final String NAME = "Example Name";

    private static final String TYPE = "Example Type";

    private static final String GENDER = "Example Gender";

    public static ArtistTestBuilder init() {
        return new ArtistTestBuilder();
    }

    public static Artist createDefaultArtist() {
        return init()
                .buildModelWithDefaultValues()
                .build();
    }

    public static ArtistDTO createDefaultArtistDTO() {
        return init()
                .buildDTOWithDefaultValues()
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
