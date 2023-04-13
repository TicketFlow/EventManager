package com.ticketflow.eventmanager.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
public class GlobalTestConfiguration {

    @MockBean
    public JwtDecoder jwtDecoder;

}
