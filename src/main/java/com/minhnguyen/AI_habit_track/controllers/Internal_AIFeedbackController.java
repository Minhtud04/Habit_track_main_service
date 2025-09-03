package com.minhnguyen.AI_habit_track.controllers;

import com.minhnguyen.AI_habit_track.DTO.Internal.AI_FeedbackDTO;
import com.minhnguyen.AI_habit_track.services.sub_services.AI_FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class Internal_AIFeedbackController {
    private static final Logger LOG = LoggerFactory.getLogger(Internal_AIFeedbackController.class);
    private final AI_FeedbackService aiFeedbackService;

    public Internal_AIFeedbackController(AI_FeedbackService aiFeedbackService) {
        this.aiFeedbackService = aiFeedbackService;
    }


    //This does not have false case?
    //

    @PostMapping("/service-internal/aifeedback")
    public ResponseEntity<Void> receiveAiFeedback(@RequestBody AI_FeedbackDTO payload) {
        LOG.info("Received  AI feedback request - session ID: {}", payload.getSessionId());

        aiFeedbackService.saveFeedback(payload.getSessionId(), payload);
        return ResponseEntity.ok().build();
    }
}