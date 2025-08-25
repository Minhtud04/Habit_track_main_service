package com.minhnguyen.AI_habit_track.controllers;

import com.minhnguyen.AI_habit_track.DTO.Internal.FullFocusSessionDTO;
import com.minhnguyen.AI_habit_track.DTO.Request.FocusSessionRequestDTO;
import com.minhnguyen.AI_habit_track.DTO.Response.FocusSessionInstantResponseDTO;

import com.minhnguyen.AI_habit_track.DTO.Response.FocusSessionResultResponseDTO;
import com.minhnguyen.AI_habit_track.models.FocusSession;
import com.minhnguyen.AI_habit_track.services.FocusSessionOrchestratorService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class FocusSessionController {
    private static final Logger logger = LoggerFactory.getLogger(FocusSessionController.class);
    private final FocusSessionOrchestratorService orchestratorService;
    public FocusSessionController(FocusSessionOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @PostMapping("/services/sessions")
    public ResponseEntity<FocusSessionInstantResponseDTO> createFocusSession(@Valid @RequestBody FocusSessionRequestDTO requestDTO) {
        // Use a standard logging method like .info(), .debug(), .error(), etc.
        logger.info("Controller: Received request to create a focus session.");

        FocusSession savedSession = orchestratorService.processAndSaveWorkSession(requestDTO);

        return ResponseEntity.ok(new FocusSessionInstantResponseDTO(
                200,
                "Focus session created successfully",
                savedSession.getId())
        );
    }


    @GetMapping("/services/sessions/{sessionId}")
    public ResponseEntity<FocusSessionResultResponseDTO> getFocusSession(@PathVariable Long sessionId) {
        logger.info("Controller: Received request to retrieve focus session with ID: {}", sessionId);

        FullFocusSessionDTO fullFocusSession = orchestratorService.getFullFocusSessionResponse(sessionId);
        if (fullFocusSession == null) {
            logger.error("AI feedback is not ready ! Please try again ! (ID: {})", sessionId);
            return ResponseEntity.ok(new FocusSessionResultResponseDTO(
                    200,
                    "Pending",
                    "AI feedback is not ready! Please try again later.",
                    null));
        }
        return ResponseEntity.ok(new FocusSessionResultResponseDTO(
                200,
                "Ready",
                "Focus session retrieved successfully",
                fullFocusSession));
    }
}