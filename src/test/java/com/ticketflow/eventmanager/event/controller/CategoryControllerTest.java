package com.ticketflow.eventmanager.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketflow.eventmanager.event.controller.dto.CategoryDTO;
import com.ticketflow.eventmanager.event.service.CategoryService;
import com.ticketflow.eventmanager.testbuilder.CategoryTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void checkout_shouldCreateCategory() throws Exception {
        CategoryDTO categoryDTO = CategoryTestBuilder.createDefaultCategoryDTO();
        CategoryDTO categoryToCompare = CategoryTestBuilder.createDefaultCategoryDTO();
        categoryToCompare.setId(1L);

        when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(categoryToCompare);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(categoryDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(categoryDTO.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ageGroup").value(categoryDTO.getAgeGroup()));
    }
}
