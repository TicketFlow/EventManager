package com.ticketflow.eventmanager.event.service;


import com.ticketflow.eventmanager.event.controller.dto.CategoryDTO;
import com.ticketflow.eventmanager.event.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ticketflow.eventmanager.event.model.Category;
import com.ticketflow.eventmanager.testbuilder.CategoryTestBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        categoryService = new CategoryService(categoryRepository, modelMapper);
    }

    @Test
    public void testCreateCategory() {
        CategoryDTO categoryDTO = CategoryTestBuilder.createDefaultCategoryDTO();
        Category categoryToSave = CategoryTestBuilder.init()
                .buildModelWithDefaultValues()
                .build();

        Category categorySaved = CategoryTestBuilder.init()
                .buildModelWithDefaultValues()
                .id(2L)
                .build();

        when(categoryRepository.save(categoryToSave)).thenReturn(categorySaved);

        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(categoryDTO.getName(), result.getName());
        assertEquals(categoryDTO.getDescription(), result.getDescription());
        assertEquals(categoryDTO.getAgeGroup(), result.getAgeGroup());
    }

    @Test
    public void testFindById() {
        Long categoryId = 1L;
        Category category = CategoryTestBuilder.init()
                .buildModelWithDefaultValues()
                .id(categoryId)
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(categoryId);

        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getDescription(), result.getDescription());
        assertEquals(category.getAgeGroup(), result.getAgeGroup());
    }
}