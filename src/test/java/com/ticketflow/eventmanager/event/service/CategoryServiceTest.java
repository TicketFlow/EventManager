package com.ticketflow.eventmanager.event.service;


import com.ticketflow.eventmanager.event.controller.dto.CategoryDTO;
import com.ticketflow.eventmanager.event.controller.filter.CategoryFilter;
import com.ticketflow.eventmanager.event.exception.CategoryException;
import com.ticketflow.eventmanager.event.exception.NotFoundException;
import com.ticketflow.eventmanager.event.exception.util.CategoryErrorCode;
import com.ticketflow.eventmanager.event.model.Category;
import com.ticketflow.eventmanager.event.model.Event;
import com.ticketflow.eventmanager.event.repository.CategoryRepository;
import com.ticketflow.eventmanager.event.repository.EventRepository;
import com.ticketflow.eventmanager.testbuilder.CategoryTestBuilder;
import com.ticketflow.eventmanager.testbuilder.EventTestBuilder;
import com.ticketflow.eventmanager.utils.GlobalTestConfiguration;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
@Import(GlobalTestConfiguration.class)
class CategoryServiceTest {

    private static final Long CATEGORY_ID = 1L;
    private static final String USER_ID = "userId";
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private JwtUserAuthenticationService jwtUserAuthenticationService;
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        categoryService = new CategoryService(categoryRepository, eventRepository, jwtUserAuthenticationService, modelMapper);
    }

    @Test
    void getAll_shouldReturnListOfCategoryDTO() {
        List<Category> categoryList = mountCategoryEntityList();
        List<CategoryDTO> expectedCategoryDTOList = mountCategoryListDTO();

        CategoryFilter categoryFilter = CategoryTestBuilder
                .buildCategoryFilterWithDefaultValues()
                .build();

        when(categoryRepository.findAll(ArgumentMatchers.<Specification<Category>>any())).thenReturn(categoryList);

        List<CategoryDTO> actualCategoryDTOList = categoryService.getAll(categoryFilter);

        assertThat(actualCategoryDTOList).usingRecursiveComparison().isEqualTo(expectedCategoryDTOList);

        verify(categoryRepository).findAll(ArgumentMatchers.<Specification<Category>>any());

    }

    @Test
    void createCategory_IfCategoryIsValid_ShouldCreateCategory() {
        CategoryDTO categoryDTO = CategoryTestBuilder.createDefaultCategoryDTO();

        Category category = CategoryTestBuilder
                .buildModelWithDefaultValues()
                .owner(null)
                .build();


        when(categoryRepository.findByName(categoryDTO.getName())).thenReturn(category);
        when(jwtUserAuthenticationService.getCurrentUserId()).thenReturn(USER_ID);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category savedCategory = invocation.getArgument(0);
            validateCategoryFields(categoryDTO, savedCategory);

            savedCategory.setId(category.getId());
            return savedCategory;
        });
        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(categoryDTO.getName(), result.getName());
        assertEquals(categoryDTO.getDescription(), result.getDescription());
        assertEquals(categoryDTO.getAgeGroup(), result.getAgeGroup());
    }

    private void validateCategoryFields(CategoryDTO categoryDTO, Category savedCategory) {
        assertThat(savedCategory.getOwner()).isEqualTo(USER_ID);
        assertThat(savedCategory.getName()).isEqualTo(categoryDTO.getName());
        assertThat(savedCategory.getDescription()).isEqualTo(categoryDTO.getDescription());
        assertThat(savedCategory.getAgeGroup()).isEqualTo(categoryDTO.getAgeGroup());
    }

    @Test
    void findById_IfCategoryExists_ShouldReturnCategory() {
        Long categoryId = 1L;
        Category category = CategoryTestBuilder
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

        CategoryDTO categoryDTO = CategoryTestBuilder
                .buildDTOWithDefaultValues()
                .description("New description")
                .name("New name")
                .ageGroup("Adult")
                .owner("1")
                .build();

        Category categorySaved = CategoryTestBuilder
                .buildModelWithDefaultValues()
                .description("New description")
                .name("New name")
                .ageGroup("Adult")
                .owner("1")
                .build();

        when(categoryRepository.findById(categoryDTO.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(categorySaved)).thenReturn(categorySaved);
        when(jwtUserAuthenticationService.getCurrentUserId()).thenReturn(categoryDTO.getOwner());


        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO);

        assertEquals(categoryDTO.getId(), updatedCategoryDTO.getId());
        assertEquals(categoryDTO.getAgeGroup(), updatedCategoryDTO.getAgeGroup());
        assertEquals(categoryDTO.getDescription(), updatedCategoryDTO.getDescription());
        assertEquals(categoryDTO.getName(), updatedCategoryDTO.getName());

        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void updateCategory_withDifferentOwner_ShouldThrowCategoryException() {
        Category category = CategoryTestBuilder.createDefaultCategory();

        CategoryDTO categoryDTO = CategoryTestBuilder
                .buildDTOWithDefaultValues()
                .description("New description")
                .name("New name")
                .ageGroup("Adult")
                .owner("1")
                .build();

        when(categoryRepository.findById(categoryDTO.getId())).thenReturn(Optional.of(category));
        when(jwtUserAuthenticationService.getCurrentUserId()).thenReturn(USER_ID);

        CategoryException exception = assertThrows(CategoryException.class, () -> categoryService.updateCategory(categoryDTO));

        MatcherAssert.assertThat(exception.getMessage(), containsString(CategoryErrorCode.CATEGORY_NOT_OWNER.getCode()));

        verify(categoryRepository, never()).save(any(Category.class));
        verify(categoryRepository, times(1)).findByName(categoryDTO.getName());
    }


    @Test
    void updateCategory_WithEmptyFields_IfCategoryExists_ShouldUpdateCategory() {
        Category category = CategoryTestBuilder.createDefaultCategory();

        CategoryDTO categoryDTO = CategoryTestBuilder
                .buildDTOWithDefaultValues()
                .description("")
                .name("")
                .ageGroup("")
                .build();

        Category categorySaved = CategoryTestBuilder
                .buildModelWithDefaultValues()
                .build();

        when(categoryRepository.findById(categoryDTO.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(categorySaved)).thenReturn(categorySaved);
        when(jwtUserAuthenticationService.getCurrentUserId()).thenReturn(categoryDTO.getOwner());

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
        Category category = CategoryTestBuilder
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
        CategoryDTO categoryDTO = CategoryTestBuilder
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
        when(jwtUserAuthenticationService.getCurrentUserId()).thenReturn(categoryDTO.getOwner());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> categoryService.updateCategory(categoryDTO));

        MatcherAssert.assertThat(exception.getMessage(), containsString(CategoryErrorCode.CATEGORY_NOT_FOUND.getCode()));
        MatcherAssert.assertThat(exception.getMessage(), containsString(categoryDTO.getId().toString()));

        verify(categoryRepository, times(1)).findById(categoryDTO.getId());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void deleteCategory_IfCategoryExistsAndIsNotBeingUsedAndExists_ShouldDeleteCategory() {
        Category category = CategoryTestBuilder.createDefaultCategory();

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(eventRepository.findAllByCategoryId(category.getId())).thenReturn(Collections.emptyList());
        when(jwtUserAuthenticationService.getCurrentUserId()).thenReturn(category.getOwner());

        categoryService.deleteCategory(category.getId());

        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteCategory_IfCategoryDoesNotExist_ShouldThrowNotFoundException() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> categoryService.deleteCategory(CATEGORY_ID));

        MatcherAssert.assertThat(exception.getMessage(), containsString(CategoryErrorCode.CATEGORY_NOT_FOUND.getCode()));
        MatcherAssert.assertThat(exception.getMessage(), containsString(CATEGORY_ID.toString()));

        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void deleteCategory_IfCategoryIsBeingUsed_ShouldThrowCategoryException() {
        Category category = CategoryTestBuilder.createDefaultCategory();
        Event event = EventTestBuilder.createDefaultEvent();
        List<Event> events = Collections.singletonList(event);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(eventRepository.findAllByCategoryId(category.getId())).thenReturn(events);

        CategoryException exception = assertThrows(CategoryException.class,
                () -> categoryService.deleteCategory(category.getId()));

        MatcherAssert.assertThat(exception.getMessage(), containsString(CategoryErrorCode.CATEGORY_BEING_USED.getCode()));
        MatcherAssert.assertThat(exception.getMessage(), containsString(category.getId().toString()));
        MatcherAssert.assertThat(exception.getMessage(), containsString(event.getId().toString()));

        verify(categoryRepository, never()).delete(any(Category.class));

    }

    @Test
    void checkIfCategoryIdBeingUsed_IfEventsListIsEmpty_ShouldNotThrowException() {
        when(eventRepository.findAllByCategoryId(CATEGORY_ID)).thenReturn(Collections.emptyList());

        categoryService.checkIfCategoryIdBeingUsed(CATEGORY_ID);

        verify(eventRepository).findAllByCategoryId(CATEGORY_ID);
    }


    private List<Category> mountCategoryEntityList() {
        Category category1 = CategoryTestBuilder
                .buildModelWithDefaultValues()
                .id(1L)
                .build();

        Category category2 = CategoryTestBuilder
                .buildModelWithDefaultValues()
                .id(2L)
                .build();

        return List.of(category1, category2);
    }

    private List<CategoryDTO> mountCategoryListDTO() {
        CategoryDTO categoryDTO1 = CategoryTestBuilder
                .buildDTOWithDefaultValues()
                .id(1L)
                .build();

        CategoryDTO categoryDTO2 = CategoryTestBuilder
                .buildDTOWithDefaultValues()
                .id(2L)
                .build();

        return List.of(categoryDTO1, categoryDTO2);
    }

}