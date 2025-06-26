package com.minhnguyen.AI_habit_track.controllers;

import com.minhnguyen.AI_habit_track.DTO.FocusSessionRequestDTO;
import com.minhnguyen.AI_habit_track.services.FocusSessionOrchestratorService;
import com.minhnguyen.AI_habit_track.utils.Log.Logger;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


@RestController
public class FocusSessionController {
    private Logger logger = new Logger("Session Controller");

    private final FocusSessionOrchestratorService orchestratorService;

    public FocusSessionController(FocusSessionOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @PostMapping("/v1/sessions")
    public ResponseEntity<String> createFocusSession(@Valid @RequestBody FocusSessionRequestDTO requestDTO) {
        logger.log("Received request to create a focus session.");


        orchestratorService.processAndSaveWorkSession(requestDTO);
        return ResponseEntity.ok("Session received and is being processed.");
    }
}