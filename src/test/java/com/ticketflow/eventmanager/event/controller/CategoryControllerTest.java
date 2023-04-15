package com.ticketflow.eventmanager.event.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketflow.eventmanager.event.controller.dto.CategoryDTO;
import com.ticketflow.eventmanager.event.exception.CategoryException;
import com.ticketflow.eventmanager.event.exception.handler.ControllerExceptionHandler;
import com.ticketflow.eventmanager.event.exception.util.CategoryErrorCode;
import com.ticketflow.eventmanager.event.model.Event;
import com.ticketflow.eventmanager.event.repository.EventRepository;
import com.ticketflow.eventmanager.event.service.CategoryService;
import com.ticketflow.eventmanager.event.service.JwtUserAuthenticationService;
import com.ticketflow.eventmanager.testbuilder.CategoryTestBuilder;
import com.ticketflow.eventmanager.testbuilder.EventTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private CategoryController categoryController;


    private JwtUserAuthenticationService jwtUserAuthenticationService;

    @BeforeEach
    public void setup() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages_en");
        messageSource.setDefaultEncoding("UTF-8");

        jwtUserAuthenticationService = Mockito.mock(JwtUserAuthenticationService.class);
        ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler(messageSource, jwtUserAuthenticationService);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(controllerExceptionHandler)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getAll_shouldReturnListOfCategoryDTO() throws Exception {
        CategoryDTO categoryDTO1 = CategoryTestBuilder.init()
                .buildDTOWithDefaultValues()
                .id(1L)
                .build();

        CategoryDTO categoryDTO2 = CategoryTestBuilder.init()
                .buildDTOWithDefaultValues()
                .id(2L)
                .build();

        List<CategoryDTO> expectedCategoryDTOs = Arrays.asList(categoryDTO1, categoryDTO2);

        when(categoryService.getAll()).thenReturn(expectedCategoryDTOs);

        MvcResult result = mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<CategoryDTO> actualCategoryDTOs = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertThat(actualCategoryDTOs).usingRecursiveComparison().isEqualTo(expectedCategoryDTOs);
        verify(categoryService).getAll();
    }


    @Test
    void create_shouldReturnCreatedCategoryDTO() throws Exception {
        CategoryDTO categoryDTO = CategoryTestBuilder.createDefaultCategoryDTO();
        CategoryDTO expectedCategoryDTO = CategoryTestBuilder.createDefaultCategoryDTO();

        when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(expectedCategoryDTO);

        MvcResult result = mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDTO actualCategoryDTO = objectMapper.readValue(jsonResponse, CategoryDTO.class);

        assertThat(actualCategoryDTO).usingRecursiveComparison().isEqualTo(expectedCategoryDTO);
        assertThat(actualCategoryDTO.getId()).isNotNull();
        verify(categoryService).createCategory(any(CategoryDTO.class));
    }

    @Test
    void create_shouldThrowCategoryException_CategoryNameDuplicated() throws Exception {
        CategoryDTO categoryDTO = CategoryTestBuilder.createDefaultCategoryDTO();

        when(categoryService.createCategory(any(CategoryDTO.class))).thenThrow(new CategoryException(CategoryErrorCode.CATEGORY_NAME_DUPLICATED.withParams(categoryDTO.getName())));
        Mockito.when(jwtUserAuthenticationService.getCurrentUserLocale()).thenReturn(Locale.ENGLISH);

        MvcResult result = mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorCode = CategoryErrorCode.CATEGORY_NAME_DUPLICATED.getCode();

        assertTrue(jsonResponse.contains(errorCode));
        assertTrue(jsonResponse.contains("Category with name " + categoryDTO.getName() + " already registered."));

        verify(categoryService).createCategory(any(CategoryDTO.class));
    }

    @Test
    void update_shouldUpdateCategory() throws Exception {
        CategoryDTO categoryDTO = CategoryTestBuilder.createDefaultCategoryDTO();
        CategoryDTO expectedCategoryDTO = CategoryTestBuilder.init()
                .buildDTOWithDefaultValues()
                .name("New name")
                .build();

        when(categoryService.updateCategory(any(CategoryDTO.class))).thenReturn(expectedCategoryDTO);

        MvcResult result = mockMvc.perform(put("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDTO actualCategoryDTO = objectMapper.readValue(jsonResponse, CategoryDTO.class);

        assertThat(actualCategoryDTO).usingRecursiveComparison().isEqualTo(expectedCategoryDTO);
        verify(categoryService).updateCategory(any(CategoryDTO.class));
    }

    @Test
    void update_shouldThrowCategoryException_categoryIdRequired() throws Exception {
        CategoryDTO categoryDTO = CategoryTestBuilder.createDefaultCategoryDTO();
        CategoryDTO expectedCategoryDTO = CategoryTestBuilder.init()
                .buildDTOWithDefaultValues()
                .name("New name")
                .build();

        when(categoryService.updateCategory(any(CategoryDTO.class))).thenThrow(new CategoryException(CategoryErrorCode.CATEGORY_ID_REQUIRED.withParams()));
        Mockito.when(jwtUserAuthenticationService.getCurrentUserLocale()).thenReturn(Locale.ENGLISH);

        MvcResult result = mockMvc.perform(put("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorCode = CategoryErrorCode.CATEGORY_ID_REQUIRED.getCode();

        assertTrue(jsonResponse.contains(errorCode));
        assertTrue(jsonResponse.contains("Category id cannot be empty."));
        verify(categoryService).updateCategory(any(CategoryDTO.class));
    }

    @Test
    void delete_shouldDeleteCategory() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete("/category/{id}", categoryId))
                .andExpect(status().isOk());

        verify(categoryService).deleteCategory(categoryId);
    }

    @Test
    void update_shouldThrowCategoryException_categoryNotFound() throws Exception {
        Long nonExistingCategoryId = 1L;

        doThrow(new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND.withParams(nonExistingCategoryId)))
                .when(categoryService).deleteCategory(anyLong());

        Mockito.when(jwtUserAuthenticationService.getCurrentUserLocale()).thenReturn(Locale.ENGLISH);

        MvcResult result = mockMvc.perform(delete("/category/{id}", nonExistingCategoryId))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorCode = CategoryErrorCode.CATEGORY_NOT_FOUND.getCode();

        assertTrue(jsonResponse.contains(errorCode));
        assertTrue(jsonResponse.contains("Category not found."));
        verify(categoryService).deleteCategory(nonExistingCategoryId);
    }

    @Test
    void update_shouldThrowCategoryException_categoryBeingUsed() throws Exception {
        Long categoryId = 5L;

        List<Event> events = new ArrayList<>();
        Event event = EventTestBuilder.init()
                .buildModelWithDefaultValues()
                .build();
        events.add(event);

        List<String> eventIds = events.stream()
                .map(Event::getId)
                .map(String::valueOf)
                .toList();

        doThrow(new CategoryException(CategoryErrorCode.CATEGORY_BEING_USED.withParams(categoryId, eventIds)))
                .when(categoryService).deleteCategory(anyLong());

        Mockito.when(jwtUserAuthenticationService.getCurrentUserLocale()).thenReturn(Locale.ENGLISH);

        MvcResult result = mockMvc.perform(delete("/category/{id}", categoryId))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorCode = CategoryErrorCode.CATEGORY_BEING_USED.getCode();

        assertTrue(jsonResponse.contains(errorCode));
        assertTrue(jsonResponse.contains("Category " + categoryId + " is being used on events: " + eventIds + "."));
        verify(categoryService).deleteCategory(categoryId);
    }


}
