package com.minhnguyen.AI_habit_track.controllers;

import com.minhnguyen.AI_habit_track.DTO.ActivitiesFlowDTO.FocusSessionRequestDTO;
import com.minhnguyen.AI_habit_track.models.FocusSession;
import com.minhnguyen.AI_habit_track.services.FocusSessionOrchestratorService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class FocusSessionController {
    // Use the standard SLF4J Logger and LoggerFactory
    private static final Logger logger = LoggerFactory.getLogger(FocusSessionController.class);
    private final FocusSessionOrchestratorService orchestratorService;
    public FocusSessionController(FocusSessionOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @PostMapping("/v1/sessions")
    public ResponseEntity<String> createFocusSession(@Valid @RequestBody FocusSessionRequestDTO requestDTO) {
        // Use a standard logging method like .info(), .debug(), .error(), etc.
        logger.info("Controller: Received request to create a focus session.");

        FocusSession savedSession = orchestratorService.processAndSaveWorkSession(requestDTO);

        return ResponseEntity.ok("Session received and is being processed.");
    }
}