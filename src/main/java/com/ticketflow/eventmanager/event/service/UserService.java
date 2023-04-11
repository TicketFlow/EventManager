package com.ticketflow.eventmanager.event.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sub = (String) ((JwtAuthenticationToken) authentication).getTokenAttributes().get("sub");
//        String userId = sub.substring(sub.lastIndexOf("/") + 1);
        return sub;
    }
}