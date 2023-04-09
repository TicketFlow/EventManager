package com.ticketflow.eventmanager.event.service;

import com.ticketflow.eventmanager.event.controller.dto.ArtistDTO;
import com.ticketflow.eventmanager.event.exception.EventException;
import com.ticketflow.eventmanager.event.exception.NotFoundException;
import com.ticketflow.eventmanager.event.exception.util.ArtistErrorCode;
import com.ticketflow.eventmanager.event.model.Artist;
import com.ticketflow.eventmanager.event.repository.ArtistRepository;
import com.ticketflow.eventmanager.testbuilder.ArtistTestBuilder;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
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
    void createArtist_ifArtistIsValid_ShouldCreateArtistSuccessfully() {
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
    void createArtist_IfArtistAlreadyRegistered_ShouldThrowException() {
        ArtistDTO artistDTO = ArtistTestBuilder.createDefaultArtistDTO();

        when(artistRepository.existsByName(artistDTO.getName())).thenReturn(true);

        Exception exception = assertThrows(EventException.class,
                () -> artistService.createArtist(artistDTO));

        MatcherAssert.assertThat(exception.getMessage(), containsString(ArtistErrorCode.ARTIST_ALREADY_REGISTERED.getCode()));
        MatcherAssert.assertThat(exception.getMessage(), containsString(artistDTO.getName()));

        verify(artistRepository, never()).save(any());
    }

    @Test
    void findByIds_ShouldReturnListOfArtists() {
        List<Long> artistIds = new ArrayList<>();
        artistIds.add(1L);
        artistIds.add(2L);

        Artist artist1 = ArtistTestBuilder.init()
                .buildModelWithDefaultValues()
                .id(1L)
                .build();

        Artist artist2 = ArtistTestBuilder.init()
                .buildModelWithDefaultValues()
                .id(2L)
                .build();

        List<Artist> artists = List.of(artist1, artist2);

        when(artistRepository.findAllById(anyList())).thenReturn(artists);

        List<Artist> result = artistService.findByIds(artistIds);

        assertNotNull(result);
        assertEquals(artists.size(), result.size());
    }

    @Test
    void findByIds_WhenAnArtistDoesNotExist_ThrowNotFoundException() {
        List<Long> artistIds = new ArrayList<>();
        artistIds.add(1L);
        artistIds.add(2L);

        List<Artist> artists = new ArrayList<>();
        artists.add(ArtistTestBuilder.createDefaultArtist());

        when(artistRepository.findAllById(anyList())).thenReturn(artists);

        Exception exception = assertThrows(NotFoundException.class,
                () -> artistService.findByIds(artistIds));

        MatcherAssert.assertThat(exception.getMessage(), containsString(ArtistErrorCode.ARTISTS_NOT_FOUND.getCode()));
        MatcherAssert.assertThat(exception.getMessage(), containsString(artistIds.get(1).toString()));
    }

    @Test
    void findByIds_ShouldThrowNotFoundException_WhenNoArtistsFound() {
        List<Long> artistIds = new ArrayList<>();
        artistIds.add(1L);
        artistIds.add(2L);

        List<Artist> artists = new ArrayList<>();

        when(artistRepository.findAllById(anyList())).thenReturn(artists);

        Exception exception = assertThrows(NotFoundException.class,
                () -> artistService.findByIds(artistIds));

        MatcherAssert.assertThat(exception.getMessage(), containsString(ArtistErrorCode.ARTISTS_NOT_FOUND.getCode()));
        MatcherAssert.assertThat(exception.getMessage(), containsString(artistIds.get(0).toString()));
        MatcherAssert.assertThat(exception.getMessage(), containsString(artistIds.get(1).toString()));
    }
}
