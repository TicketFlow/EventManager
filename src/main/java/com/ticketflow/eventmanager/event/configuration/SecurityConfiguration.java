package com.ticketflow.eventmanager.event.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String ENDPOINT_CATEGORY = "/category";
    private static final String ENDPOINT_ARTIST = "/artist";
    private static final String ENDPOINT_EVENT = "/event";
    private static final String ROLE_USER = "user";
    private static final String ROLE_EVENT_MANAGER = "event_manager";

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfiguration(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .formLogin()
                .and()
                .cors()
                .and()
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, ENDPOINT_CATEGORY).hasAnyRole(ROLE_USER, ROLE_EVENT_MANAGER)
                        .requestMatchers(HttpMethod.POST, ENDPOINT_CATEGORY).hasRole(ROLE_EVENT_MANAGER)
                        .requestMatchers(HttpMethod.PUT, ENDPOINT_CATEGORY).hasRole(ROLE_EVENT_MANAGER)
                        .requestMatchers(HttpMethod.DELETE, ENDPOINT_CATEGORY).hasRole(ROLE_EVENT_MANAGER)

                        .requestMatchers(HttpMethod.POST, ENDPOINT_ARTIST).hasRole(ROLE_EVENT_MANAGER)

                        .requestMatchers(HttpMethod.POST, ENDPOINT_EVENT).hasRole(ROLE_EVENT_MANAGER)
                        .anyRequest().authenticated())

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt()
                        .jwtAuthenticationConverter(getJwtAuthenticationConverter()));

        return http.build();
    }

    JwtAuthenticationConverter getJwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("roles");
        converter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return authenticationConverter;
    }


}
