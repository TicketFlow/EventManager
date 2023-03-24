package com.ticketflow.eventmanager.event.repository;

import com.ticketflow.eventmanager.event.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    boolean existsByName(String name);

}
