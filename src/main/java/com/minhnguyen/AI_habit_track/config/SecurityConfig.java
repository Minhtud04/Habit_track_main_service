package com.minhnguyen.AI_habit_track.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler) throws Exception {
        // Disable CSRF protection for all endpoints
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // --- ADD THIS SECTION FOR H2 CONSOLE ---
                .csrf(csrf -> csrf
                        // Disable CSRF protection only for the H2 console path
                        .ignoringRequestMatchers(toH2Console())
                )
                .headers(headers -> headers
                        // The H2 console runs in a frame, so we need to allow it
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                // --- END OF H2 CONSOLE SECTION ---

                .authorizeHttpRequests(auth -> auth
                        // Allow public access to the essentials
                        .requestMatchers("/health", "/oauth2/**").permitAll()
                        // Secure everything else
                        .anyRequest().authenticated()
                )

                //disable csrf
                .csrf(AbstractHttpConfigurer::disable
                )

                // Configure OAuth2 Login with just the essential success handler
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(successHandler)
                );

        return http.build();
    }

    // CORS configuration remains essential for your SPA architecture
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
        // A @Bean method must return an object. Here, we return the handler itself.
        return (request, response, authentication) -> {

            // The logic to get the user goes INSIDE the lambda.
            // The 'authentication' object is passed into the handler when it's called.
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oauthUser = oauthToken.getPrincipal();

            // You can now use the user's details for logging or other tasks.
            String name = oauthUser.getAttribute("name");
            String email = oauthUser.getAttribute("email");

            System.out.println("Login successful for user: " + name + " with email: " + email);

            // Finally, the main job of the handler: redirect the user's browser.
            response.sendRedirect(OAUTH2_LOGIN_SUCCESS_URL);
        };
    }
}