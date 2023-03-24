package com.ticketflow.eventmanager.event.service;

import com.ticketflow.eventmanager.event.controller.dto.ArtistDTO;
import com.ticketflow.eventmanager.event.exception.EventException;
import com.ticketflow.eventmanager.event.exception.util.EventErrorCode;
import com.ticketflow.eventmanager.event.model.Artist;
import com.ticketflow.eventmanager.event.repository.ArtistRepository;
import com.ticketflow.eventmanager.testbuilder.ArtistTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    private ArtistService artistService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        artistService = new ArtistService(artistRepository, modelMapper);
    }

    @Test
    public void createArtist_ifArtistIsValid_ShouldCreateArtistSuccessfully() {
        ArtistDTO artistDTO = ArtistTestBuilder.createDefaultArtistDTO();
        Artist artistToSave = ArtistTestBuilder.createDefaultArtist();
        Artist artistSaved = ArtistTestBuilder.createDefaultArtist();

        when(artistRepository.save(artistToSave)).thenReturn(artistSaved);

        ArtistDTO createdArtistDTO = artistService.createArtist(artistDTO);

        assertNotNull(createdArtistDTO.getId());
        assertEquals(artistDTO.getName(), createdArtistDTO.getName());
        assertEquals(artistDTO.getGender(), createdArtistDTO.getGender());
    }

    @Test
    public void createArtist_IfArtistAlreadyRegistered_ShouldThrowException() {
        ArtistDTO artistDTO = ArtistTestBuilder.createDefaultArtistDTO();

        when(artistRepository.existsByName(artistDTO.getName())).thenReturn(true);

        Exception exception = assertThrows(EventException.class,
                () -> artistService.createArtist(artistDTO));

        String expectedErrorCode = EventErrorCode.ARTIST_ALREADY_REGISTERED.getCode();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedErrorCode));
        assertTrue(actualMessage.contains(artistDTO.getName()));

        verify(artistRepository, never()).save(any());

    }

}
