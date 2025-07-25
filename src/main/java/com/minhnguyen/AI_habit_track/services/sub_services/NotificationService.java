package com.minhnguyen.AI_habit_track.services.sub_services;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.minhnguyen.AI_habit_track.DTO.AIFlowDTO.AI_FeedbackResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.Principal;
import org.springframework.security.oauth2.core.user.OAuth2User; // Import OAuth2User
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();       //store in-memory
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String email = getCredentials(session);

        if (email == null) {
            logger.warn("WebSocket connection attempted without an authenticated OAuth2 user. Closing session.");
            session.close(CloseStatus.POLICY_VIOLATION.withReason("User must be an authenticated OAuth2 user"));
            return;
        }

        sessions.put(email, session);
        logger.info("WebSocket connection established for user: {}", email);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String email = getCredentials(session);
        if (email != null) {
            sessions.remove(email);
            logger.info("WebSocket connection closed for user: {}. Status: {}", email, status);
        }
    }


    /**
     * Sends the AI feedback to a specific user if they have an active WebSocket session.
     * @param feedback The feedback data to send.
     */
    public void sendFeedbackUpdate(AI_FeedbackResponseDTO feedback) {
        String email = feedback.getEmail();
        WebSocketSession session = sessions.get(email);
        if (session != null && session.isOpen()) {
            try {
                String messagePayload = objectMapper.writeValueAsString(feedback);
                session.sendMessage(new TextMessage(messagePayload));               //Send binary message

                logger.info("Successfully sent ai feedback to user: {}", email);
            } catch (IOException e) {
                logger.error("Failed to send WebSocket message to user: {}", email, e);
            }

        } else {
            logger.warn("No active WebSocket session found for user: {}. Cannot send update.", email);
        }
    }

    private String getCredentials(WebSocketSession session) {
        Principal principal = session.getPrincipal();
        if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            return oauth2User.getAttribute("email");
        }
        return null; // Return null if the principal is not the expected type
    }
}

