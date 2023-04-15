package com.ticketflow.eventmanager.event.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class JwtUserAuthenticationServiceTest {

    @InjectMocks
    private JwtUserAuthenticationService jwtUserAuthenticationService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getCurrentUserId() {
        Map<String, Object> tokenAttributes = new HashMap<>();
        tokenAttributes.put("sub", "test-user-id");

        when(securityContext.getAuthentication()).thenReturn(jwtAuthenticationToken);
        when(jwtAuthenticationToken.getTokenAttributes()).thenReturn(tokenAttributes);

        assertEquals("test-user-id", jwtUserAuthenticationService.getCurrentUserId());
    }

    @Test
    void getCurrentUserLocale() {
        Map<String, Object> tokenAttributes = new HashMap<>();
        tokenAttributes.put("locale", "en-US");

        when(securityContext.getAuthentication()).thenReturn(jwtAuthenticationToken);
        when(jwtAuthenticationToken.getTokenAttributes()).thenReturn(tokenAttributes);

        assertEquals(Locale.forLanguageTag("en-US"), jwtUserAuthenticationService.getCurrentUserLocale());
    }

    @Test
    void getCurrentUserId_noAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> jwtUserAuthenticationService.getCurrentUserId());
    }

    @Test
    void getCurrentUserId_wrongAuthenticationType() {
        Authentication wrongAuthentication = new UsernamePasswordAuthenticationToken("user", "password");

        when(securityContext.getAuthentication()).thenReturn(wrongAuthentication);

        assertThrows(IllegalStateException.class, () -> jwtUserAuthenticationService.getCurrentUserId());
    }

    @Test
    void getCurrentUserId_wrongUserIdType() {
        Map<String, Object> tokenAttributes = new HashMap<>();
        tokenAttributes.put("sub", 12345);

        when(securityContext.getAuthentication()).thenReturn(jwtAuthenticationToken);
        when(jwtAuthenticationToken.getTokenAttributes()).thenReturn(tokenAttributes);

        assertThrows(IllegalStateException.class, () -> jwtUserAuthenticationService.getCurrentUserId());
    }

    @Test
    void getCurrentUserLocale_wrongLocaleType() {
        Map<String, Object> tokenAttributes = new HashMap<>();
        tokenAttributes.put("locale", 12345);

        when(securityContext.getAuthentication()).thenReturn(jwtAuthenticationToken);
        when(jwtAuthenticationToken.getTokenAttributes()).thenReturn(tokenAttributes);

        assertThrows(IllegalStateException.class, () -> jwtUserAuthenticationService.getCurrentUserLocale());
    }
}

