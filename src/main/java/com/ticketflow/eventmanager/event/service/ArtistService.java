package com.ticketflow.eventmanager.event.service;

import com.ticketflow.eventmanager.event.controller.dto.ArtistDTO;
import com.ticketflow.eventmanager.event.exception.ArtistException;
import com.ticketflow.eventmanager.event.exception.NotFoundException;
import com.ticketflow.eventmanager.event.exception.util.ArtistErrorCode;
import com.ticketflow.eventmanager.event.model.Artist;
import com.ticketflow.eventmanager.event.repository.ArtistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public List<Artist> findByIds(List<Long> ids) {
        List<Artist> artists = artistRepository.findAllById(ids);

        if (artists.size() != ids.size()) {
            Set<Long> notFoundIds = new HashSet<>(ids);
            artists.forEach(artist -> notFoundIds.remove(artist.getId()));
            throw new NotFoundException(ArtistErrorCode.ARTISTS_NOT_FOUND.withParams(notFoundIds));
        }

        return artists;
    }

    private Artist toModel(ArtistDTO artistDTO) {
        return modelMapper.map(artistDTO, Artist.class);
    }

    private ArtistDTO toDTO(Artist artist) {
        return modelMapper.map(artist, ArtistDTO.class);
    }

    private void validateArtist(ArtistDTO artistDTO) {
        if (artistRepository.existsByName(artistDTO.getName())) {
            throw new ArtistException(ArtistErrorCode.ARTIST_ALREADY_REGISTERED.withParams(artistDTO.getName()));
        }
    }
}
