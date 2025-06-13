package com.minhnguyen.AI_habit_track.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Endpoint: /health
 * @Description: This controller provides a health check endpoint that can be used to verify if the application is running and healthy.
 */
@RestController
public class health_check {
    @GetMapping("/health")
    public String healthCheck() {
        return "OK"; // Return a simple response indicating the application is healthy
    }
}