package com.minhnguyen.AI_habit_track.config;

import com.minhnguyen.AI_habit_track.services.sub_services.QueueService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

/**
 * This configuration is only active when the "test" profile is running.
 * It provides a mock implementation of the QueueService to prevent the
 * application from trying to connect to AWS SQS during tests.
 */
@Profile("test")
@Configuration
public class TestConfig {

    @Bean
    @Primary // This ensures our mock bean is used instead of the real one
    public QueueService queueService() {
        // Return a mock (a completely fake object) of the QueueService
        return mock(QueueService.class);
    }
}
