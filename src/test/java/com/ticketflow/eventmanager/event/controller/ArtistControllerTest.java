package com.ticketflow.eventmanager.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketflow.eventmanager.event.controller.dto.ArtistDTO;
import com.ticketflow.eventmanager.event.exception.EventException;
import com.ticketflow.eventmanager.event.exception.handler.ControllerExceptionHandler;
import com.ticketflow.eventmanager.event.exception.util.ArtistErrorCode;
import com.ticketflow.eventmanager.event.service.ArtistService;
import com.ticketflow.eventmanager.event.service.JwtUserAuthenticationService;
import com.ticketflow.eventmanager.testbuilder.ArtistTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ArtistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private ArtistController artistController;

    private ControllerExceptionHandler controllerExceptionHandler;


    @BeforeEach
    public void setup() {
        MessageSource messageSource = new ReloadableResourceBundleMessageSource();
        ((ReloadableResourceBundleMessageSource) messageSource).setBasename("classpath:messages");
        ((ReloadableResourceBundleMessageSource) messageSource).setDefaultEncoding("UTF-8");
        JwtUserAuthenticationService jwtUserAuthenticationService = Mockito.mock(JwtUserAuthenticationService.class);
        controllerExceptionHandler = new ControllerExceptionHandler(messageSource, jwtUserAuthenticationService);

        // Configure o comportamento do jwtUserAuthenticationService mockado
        Mockito.when(jwtUserAuthenticationService.getCurrentUserLocale()).thenReturn(Locale.ENGLISH);

        mockMvc = MockMvcBuilders.standaloneSetup(artistController)
                .setControllerAdvice(controllerExceptionHandler)
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

    @Test
    void createArtist_shouldThrowArtistException() throws Exception {
        ArtistDTO artistDTO = ArtistTestBuilder.createDefaultArtistDTO();

        when(artistService.createArtist(any(ArtistDTO.class))).thenAnswer(invocation -> {
            ArtistDTO passedArtistDTO = invocation.getArgument(0);
            throw new EventException(ArtistErrorCode.ARTIST_ALREADY_REGISTERED.withParams(passedArtistDTO.getName()));
        });

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(post("/artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDTO)))
                .andExpect(status().is5xxServerError())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        String errorCode = ArtistErrorCode.ARTIST_ALREADY_REGISTERED.getCode();

        assertTrue(jsonResponse.contains(errorCode));
        assertTrue(jsonResponse.contains("Artist already registered: " + artistDTO.getName() + "."));

        verify(artistService).createArtist(any(ArtistDTO.class));
    }

}