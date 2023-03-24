package com.ticketflow.eventmanager.event.service;

import com.ticketflow.eventmanager.event.controller.dto.ArtistDTO;
import com.ticketflow.eventmanager.event.exception.EventException;
import com.ticketflow.eventmanager.event.exception.util.EventErrorCode;
import com.ticketflow.eventmanager.event.model.Artist;
import com.ticketflow.eventmanager.event.repository.ArtistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Qualifier("modelMapperConfig")
    private final ModelMapper modelMapper;

    public ArtistService(ArtistRepository artistRepository, ModelMapper modelMapper) {
        this.artistRepository = artistRepository;
        this.modelMapper = modelMapper;
    }

    public ArtistDTO createArtist(ArtistDTO artistDTO) {
        validateArtist(artistDTO);

        Artist artist = toModel(artistDTO);
        Artist artistSaved = artistRepository.save(artist);
        return toDTO(artistSaved);
    }

    private Artist toModel(ArtistDTO artistDTO) {
        return modelMapper.map(artistDTO, Artist.class);
    }

    private ArtistDTO toDTO(Artist artist) {
        return modelMapper.map(artist, ArtistDTO.class);
    }

    private void validateArtist(ArtistDTO artistDTO) {
        if (artistRepository.existsByName(artistDTO.getName())) {
            throw new EventException(EventErrorCode.ARTIST_ALREADY_REGISTERED.withParams(artistDTO.getName()));
        }
    }
}
