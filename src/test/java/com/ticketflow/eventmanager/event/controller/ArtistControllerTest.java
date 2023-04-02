package com.ticketflow.eventmanager.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ticketflow.eventmanager.event.controller.dto.ArtistDTO;
import com.ticketflow.eventmanager.event.service.ArtistService;
import com.ticketflow.eventmanager.testbuilder.ArtistTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.times;

import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ArtistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private ArtistController artistController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(artistController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void createArtist_shouldReturnCreatedArtistDTO() throws Exception {
        ArtistDTO artistDTO = ArtistTestBuilder.createDefaultArtistDTO();
        ArtistDTO expectedArtistDTO = ArtistTestBuilder.createDefaultArtistDTO();

        when(artistService.createArtist(any(ArtistDTO.class))).thenReturn(expectedArtistDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(post("/artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ArtistDTO actualArtistDTO = objectMapper.readValue(jsonResponse, ArtistDTO.class);

        assertThat(actualArtistDTO)
                .usingRecursiveComparison()
                .isEqualTo(expectedArtistDTO);

        assertThat(actualArtistDTO.getId()).isNotNull();

        verify(artistService).createArtist(any(ArtistDTO.class));
    }
}