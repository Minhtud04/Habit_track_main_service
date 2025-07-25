package com.minhnguyen.AI_habit_track.controllers;

import com.minhnguyen.AI_habit_track.DTO.AIFlowDTO.AI_FeedbackDTO;
import com.minhnguyen.AI_habit_track.DTO.AIFlowDTO.AI_FeedbackResponseDTO;
import com.minhnguyen.AI_habit_track.models.FocusSession;
import com.minhnguyen.AI_habit_track.services.AIFeedbackOrchestratorService;
import com.minhnguyen.AI_habit_track.services.sub_services.NotificationService;
import com.minhnguyen.AI_habit_track.services.sub_services.AI_FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("v1/internal")
public class Internal_AIFeedbackController {

    private static final Logger LOG = LoggerFactory.getLogger(Internal_AIFeedbackController.class);
    private final AIFeedbackOrchestratorService aiFeedbackOrchestratorService;

    public Internal_AIFeedbackController(AIFeedbackOrchestratorService aiFeedbackOrchestratorService) {
        this.aiFeedbackOrchestratorService = aiFeedbackOrchestratorService;
    }

    @PostMapping("/aifeedback")
    public ResponseEntity<Void> receiveAiFeedback(@RequestBody AI_FeedbackDTO payload) {
        LOG.info("Received  AI feedback request - session ID: {}", payload.getSessionId());

        // Notify the user via WebSocket
        aiFeedbackOrchestratorService.saveAiFeedbackAndNotifyUser(payload);
        return ResponseEntity.ok().build();
    }
}