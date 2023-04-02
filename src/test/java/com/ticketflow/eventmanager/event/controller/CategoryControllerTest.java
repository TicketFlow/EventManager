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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

}
