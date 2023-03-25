package com.ticketflow.eventmanager.event.controller;

import com.ticketflow.eventmanager.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

//    todo - finalizar esse teste
//    @Test
//    public void createEvent_shouldReturnCreatedEventDTO() throws Exception {
//        CreateEventDTO createEventDTO = EventTestBuilder.createDefaultCreateEventDTO();
//        EventDTO eventDTO = EventTestBuilder.createDefaultEventDTO();
//
//        when(eventService.createEvent(any(CreateEventDTO.class))).thenReturn(eventDTO);
//
//        mockMvc.perform(post("/event")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(createEventDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.name").value(eventDTO.getName()))
//                .andExpect(jsonPath("$.description").value(eventDTO.getDescription()))
//                .andExpect(jsonPath("$.dateTime").value(eventDTO.getDateTime().toString()))
//                .andExpect(jsonPath("$.imagePath").value(eventDTO.getImagePath()))
//                .andExpect(jsonPath("$.details").value(eventDTO.getDetails()))
//                .andExpect(jsonPath("$.category.id").value(eventDTO.getCategory().getId()))
//                .andExpect(jsonPath("$.category.name").value(eventDTO.getCategory().getName()))
//                .andExpect(jsonPath("$.category.description").value(eventDTO.getCategory().getDescription()))
//                .andExpect(jsonPath("$.category.ageGroup").value(eventDTO.getCategory().getAgeGroup()));
//
//        verify(eventService).createEvent(any(CreateEventDTO.class));
//    }

}
