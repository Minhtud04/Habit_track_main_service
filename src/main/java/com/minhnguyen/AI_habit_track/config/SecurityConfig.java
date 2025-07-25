package com.minhnguyen.AI_habit_track.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    final String OAUTH2_LOGIN_SUCCESS_URL = "http://localhost:3000/home";
    final String INTERNAL_API_BASE_URL = "/internal/**";


    /**
     * Security Filter Chain for internal API endpoints (Within VPC)
     * This has a higher precedence (@Order(1)) and allows all requests without authentication.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain internalApiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(INTERNAL_API_BASE_URL)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    /**
     * Main Security Filter Chain for user-facing endpoints.
     */
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
                        // Allow public access to the essentials
                        .requestMatchers("/health", "/oauth2/**").permitAll()
//                        .requestMatchers(toH2Console()).permitAll() // Also explicitly permit H2 console
                        // Secure everything else
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .successHandler(successHandler)
                );

        return http.build();
    }

    // Your existing CORS and Success Handler beans remain unchanged.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oauthUser = oauthToken.getPrincipal();

            String name = oauthUser.getAttribute("name");
            String email = oauthUser.getAttribute("email");

            System.out.println("Login successful for user: " + name + " with email: " + email);

            response.sendRedirect(OAUTH2_LOGIN_SUCCESS_URL);
        };
    }
}
