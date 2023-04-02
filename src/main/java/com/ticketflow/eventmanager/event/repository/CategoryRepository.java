package com.ticketflow.eventmanager.event.repository;

import com.ticketflow.eventmanager.event.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);
}
