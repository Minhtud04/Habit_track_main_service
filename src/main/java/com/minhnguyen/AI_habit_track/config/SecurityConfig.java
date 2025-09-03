package com.minhnguyen.AI_habit_track.config;

import com.minhnguyen.AI_habit_track.controllers.FocusSessionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;




@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.cors.extension-url}")
    private String EXTENSION_URL;
    final String INTERNAL_API_BASE_URL = "/service-internal/**";

    Logger log = LoggerFactory.getLogger(FocusSessionController.class);

    @Bean
    @Order(1)
    public SecurityFilterChain internalApiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(INTERNAL_API_BASE_URL)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain mainFilterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/health", "/oauth2/**").permitAll()
                        .requestMatchers("/ws").permitAll()
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .successHandler(successHandler)
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(EXTENSION_URL));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler(
            @Value("${app.oauth2.login-success-url}") String successUrl) {

        return (request, response, auth) -> {
            var oauth = (OAuth2AuthenticationToken) auth;
            var user = oauth.getPrincipal();
            log.info("User logged in successfully : {}", user.getName());
            response.sendRedirect(successUrl);
        };
    }
}

