package com.ticketflow.eventmanager.event.service;


import com.ticketflow.eventmanager.event.controller.dto.CategoryDTO;
import com.ticketflow.eventmanager.event.exception.CategoryException;
import com.ticketflow.eventmanager.event.exception.NotFoundException;
import com.ticketflow.eventmanager.event.exception.util.CategoryErrorCode;
import com.ticketflow.eventmanager.event.repository.CategoryRepository;
import org.hamcrest.MatcherAssert;
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

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class CategoryServiceTest {

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
    void createCategory_IfCategoryIsValid_ShouldCreateCategory() {
        CategoryDTO categoryDTO = CategoryTestBuilder.createDefaultCategoryDTO();

        Category category = CategoryTestBuilder.init()
                .buildModelWithDefaultValues()
                .build();

        when(categoryRepository.findByName(categoryDTO.getName())).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(categoryDTO.getName(), result.getName());
        assertEquals(categoryDTO.getDescription(), result.getDescription());
        assertEquals(categoryDTO.getAgeGroup(), result.getAgeGroup());
    }


    @Test
    void findById_IfCategoryExists_ShouldReturnCategory() {
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

    @Test
    void findById_IfCategoryDoesntExists_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> categoryService.findById(1L));
        MatcherAssert.assertThat(exception.getMessage(), containsString(CategoryErrorCode.CATEGORY_NOT_FOUND.getCode()));
        MatcherAssert.assertThat(exception.getMessage(), containsString("1"));
    }

    @Test
    void updateCategory_ifCategoryExists_ShouldUpdateCategory() {
        Category category = CategoryTestBuilder.createDefaultCategory();

        CategoryDTO categoryDTO = CategoryTestBuilder.init()
                .buildDTOWithDefaultValues()
                .description("New description")
                .name("New name")
                .ageGroup("Adult")
                .build();

        Category categorySaved = CategoryTestBuilder.init()
                .buildModelWithDefaultValues()
                .description("New description")
                .name("New name")
                .ageGroup("Adult")
                .build();

        when(categoryRepository.findById(categoryDTO.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(categorySaved)).thenReturn(categorySaved);

        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO);

        assertEquals(categoryDTO.getId(), updatedCategoryDTO.getId());
        assertEquals(categoryDTO.getAgeGroup(), updatedCategoryDTO.getAgeGroup());
        assertEquals(categoryDTO.getDescription(), updatedCategoryDTO.getDescription());
        assertEquals(categoryDTO.getName(), updatedCategoryDTO.getName());

        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void updateCategory_WithEmptyfields_IfCategoryExists_ShouldUpdateCategory() {
        Category category = CategoryTestBuilder.createDefaultCategory();

        CategoryDTO categoryDTO = CategoryTestBuilder.init()
                .buildDTOWithDefaultValues()
                .description("")
                .name("")
                .ageGroup("")
                .build();

        Category categorySaved = CategoryTestBuilder.init()
                .buildModelWithDefaultValues()
                .build();

        when(categoryRepository.findById(categoryDTO.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(categorySaved)).thenReturn(categorySaved);

        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO);

        assertEquals(category.getId(), updatedCategoryDTO.getId());
        assertEquals(category.getAgeGroup(), updatedCategoryDTO.getAgeGroup());
        assertEquals(category.getDescription(), updatedCategoryDTO.getDescription());
        assertEquals(category.getName(), updatedCategoryDTO.getName());

        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void createCategory_IfCategoryNameIsDuplicatedAndCategoryIdIsDifferent_ShouldThrowCategoryException() {
        CategoryDTO categoryDTO = CategoryTestBuilder.createDefaultCategoryDTO();
        Category category = CategoryTestBuilder.init()
                .buildModelWithDefaultValues()
                .id(50L)
                .build();

        when(categoryRepository.findByName(categoryDTO.getName())).thenReturn(category);

        CategoryException exception = assertThrows(CategoryException.class, () -> categoryService.createCategory(categoryDTO));

        MatcherAssert.assertThat(exception.getMessage(), containsString(CategoryErrorCode.CATEGORY_NAME_DUPLICATED.getCode()));
        MatcherAssert.assertThat(exception.getMessage(), containsString(categoryDTO.getName()));

        verify(categoryRepository, times(1)).findByName(category.getName());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void updateCategory_IfCategoryIdIsNull_ShouldThrowCategoryException() {
        CategoryDTO categoryDTO = CategoryTestBuilder.init()
                .buildDTOWithDefaultValues()
                .id(null)
                .build();

        CategoryException exception = assertThrows(CategoryException.class, () -> categoryService.updateCategory(categoryDTO));

        MatcherAssert.assertThat(exception.getMessage(), containsString(CategoryErrorCode.CATEGORY_ID_REQUIRED.getCode()));

        verify(categoryRepository, never()).findById(anyLong());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void updateCategory_IfCategoryDoesNotExist_ShouldThrowCategoryException() {
        CategoryDTO categoryDTO = CategoryTestBuilder.createDefaultCategoryDTO();
        when(categoryRepository.findById(categoryDTO.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> categoryService.updateCategory(categoryDTO));

        MatcherAssert.assertThat(exception.getMessage(), containsString(CategoryErrorCode.CATEGORY_NOT_FOUND.getCode()));
        MatcherAssert.assertThat(exception.getMessage(), containsString(categoryDTO.getId().toString()));

        verify(categoryRepository, times(1)).findById(categoryDTO.getId());
        verify(categoryRepository, never()).save(any(Category.class));
    }

}