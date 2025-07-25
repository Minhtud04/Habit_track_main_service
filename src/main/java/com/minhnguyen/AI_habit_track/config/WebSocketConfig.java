package com.minhnguyen.AI_habit_track.config;

import com.minhnguyen.AI_habit_track.services.sub_services.NotificationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    // NotiService Injection
    private final NotificationService notificationService;

    public WebSocketConfig(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        //register: Websocket "/ws" with  NotificationService
        registry.addHandler(notificationService, "/ws")
                .setAllowedOrigins("*");
    }
}

