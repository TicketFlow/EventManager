package com.ticketflow.eventmanager.event.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class JwtUserAuthenticationService {

    public String getCurrentUserId() {
        Authentication authentication = checkToken();
        return getUserIdFromToken((JwtAuthenticationToken) authentication);
    }

    public Locale getCurrentUserLocale() {
        Authentication authentication = checkToken();
        return getLocaleFromToken((JwtAuthenticationToken) authentication);
    }

    private Locale getLocaleFromToken(JwtAuthenticationToken jwtAuthenticationToken) {
        Object userId = jwtAuthenticationToken.getTokenAttributes().get("locale");
        if (userId instanceof String stringLocale) {
            return Locale.forLanguageTag(stringLocale);
        }
        throw new IllegalStateException("User locale should be of type String");
    }

    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private Authentication checkToken() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authentication found in the security context");
        }
        if (!(authentication instanceof JwtAuthenticationToken)) {
            throw new IllegalStateException("Expected JwtAuthenticationToken, but found: " + authentication.getClass().getName());
        }
        return authentication;
    }

    private String getUserIdFromToken(JwtAuthenticationToken jwtAuthenticationToken) {
        Object userId = jwtAuthenticationToken.getTokenAttributes().get("sub");
        if (userId instanceof String stringUserId) {
            return stringUserId;
        }
        throw new IllegalStateException("User ID should be of type String");
    }

}