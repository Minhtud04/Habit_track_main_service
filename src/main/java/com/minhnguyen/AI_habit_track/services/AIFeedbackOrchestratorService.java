package com.minhnguyen.AI_habit_track.services;

import com.minhnguyen.AI_habit_track.DTO.AIFlowDTO.*;

import com.minhnguyen.AI_habit_track.controllers.Internal_AIFeedbackController;
import com.minhnguyen.AI_habit_track.models.*;
import com.minhnguyen.AI_habit_track.services.sub_services.*;
import com.minhnguyen.AI_habit_track.utils.errorHandler.ErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIFeedbackOrchestratorService {
    private static final Logger LOG = LoggerFactory.getLogger(AIFeedbackOrchestratorService.class);
    private final FocusSessionService sessionService;
    private final AI_FeedbackService aiFeedbackService; // Renamed for clarity
    private final NotificationService notificationService; // Added NotificationService
    private final AuthService authService;

    public AIFeedbackOrchestratorService(AuthService authService, FocusSessionService sessionService, AI_FeedbackService aiFeedbackService, NotificationService notificationService) {
        this.sessionService = sessionService;
        this.aiFeedbackService = aiFeedbackService;
        this.notificationService = notificationService; // Initialize
        this.authService = authService;
    }

    @Transactional
    public void saveAiFeedbackAndNotifyUser(AI_FeedbackDTO feedbackDTO) {
        Long sessionId = feedbackDTO.getSessionId();
        LOG.info("Orchestrating AI feedback for session ID: {}", sessionId);

    // Step 1: Save DB
        FocusSession updatedSession = aiFeedbackService.saveFeedback(sessionId, feedbackDTO);

    // Step 2: Noti websocket
        if (updatedSession != null && updatedSession.getUser() != null) {

            AI_FeedbackResponseDTO responseToClient = new AI_FeedbackResponseDTO();
            responseToClient.setEmail(updatedSession.getUser().getEmail());
            responseToClient.setAiFocusScore(feedbackDTO.getAiFocusScore());
            responseToClient.setAiQualityScore(feedbackDTO.getAiQualityScore());
            responseToClient.setAiNotes(feedbackDTO.getAiNotes());

            notificationService.sendFeedbackUpdate(responseToClient);
        } else {
            LOG.error("Could not send notification for session ID: " + sessionId + ". User or session not found after saving.");
        }
    }
}
