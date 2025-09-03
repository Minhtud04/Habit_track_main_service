package com.minhnguyen.AI_habit_track.services;

import com.minhnguyen.AI_habit_track.DTO.*;
import com.minhnguyen.AI_habit_track.DTO.Internal.AIServiceQueueDTO;
import com.minhnguyen.AI_habit_track.DTO.Internal.FullFocusSessionDTO;
import com.minhnguyen.AI_habit_track.DTO.Request.FocusSessionRequestDTO;
import com.minhnguyen.AI_habit_track.DTO.Response.FocusSessionResultResponseDTO;
import com.minhnguyen.AI_habit_track.controllers.FocusSessionController;
import com.minhnguyen.AI_habit_track.models.*;
import com.minhnguyen.AI_habit_track.services.sub_services.*;
import com.minhnguyen.AI_habit_track.utils.errorHandler.ErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class FocusSessionOrchestratorService {


    private static final Logger logger = LoggerFactory.getLogger(FocusSessionController.class);
    private final FocusSessionService sessionService;
    private final ActivityService activityService;
    private final QueueService queueService;
    private final AuthService authService;
    private final UserService userService;
    private final AI_FeedbackService aiFeedbackService;

    public FocusSessionOrchestratorService(UserService userService, AuthService authService, FocusSessionService sessionService, ActivityService activityService, QueueService queueService, AI_FeedbackService aiFeedbackService) {
        this.sessionService = sessionService;
        this.activityService = activityService;
        this.queueService = queueService;
        this.authService = authService;
        this.userService = userService;
        this.aiFeedbackService = aiFeedbackService;
    }

    @Transactional
    public FocusSession processAndSaveWorkSession(FocusSessionRequestDTO focusSessionRequestDTO) {
        // 1. credential info
        User user = getAuthenticatedUser();

        // 2. map DTO to entity + save focus session
        FocusSession savedSession = sessionService.saveSession(focusSessionRequestDTO, user);

        // 3. save Activities
        if (focusSessionRequestDTO.getActivities() != null && !focusSessionRequestDTO.getActivities().isEmpty()) {
            activityService.saveAllActivities(focusSessionRequestDTO, savedSession);
        }
        // 4. send to queue
        queueService.queueSessionForFeedback(new AIServiceQueueDTO(savedSession));
        savedSession.setAiFeedbackStatus(FocusSession.AiFeedbackStatus.PENDING);
        savedSession.setProcessExceededTime(Instant.now().plus(2, ChronoUnit.MINUTES));
        logger.info("Successfully saved and sent to Queue. Focus Session ID: {} - User email: {}", savedSession.getId(), savedSession.getUser().getEmail()); // Log the session ID for tracking

        return savedSession;
    }


    public FullFocusSessionDTO getFullFocusSessionResponse(Long sessionId) {
        FocusSession focusSession = sessionService.getFocusSessionById(sessionId);

        if (focusSession == null) {
            throw new ErrorException.ResourceNotFound("Focus session not found with ID: " + sessionId);
        }
        AISessionFeedback aiFeedback = aiFeedbackService.getAiFeedbackByFocusSession(focusSession);
        FullFocusSessionDTO response;
        if (aiFeedback == null) {
            response = null;
        } else {
            logger.info("AI feedback found for Focus Session ID: {}", sessionId);
            response = createFullFocusSessionResponseDTO(focusSession, aiFeedback);
        }

        return response;
    }


    private User getAuthenticatedUser() {
        UserOAuthDTO userOAuthDTO = authService.getUserDetails();
        if (userOAuthDTO == null) {
            throw new ErrorException.Unauthenticated("User is not authenticated or OAuth2 details are missing.");
        }
        return userService.findOrCreateUser(userOAuthDTO);
    }



    private FullFocusSessionDTO createFullFocusSessionResponseDTO(FocusSession focusSession, AISessionFeedback aiFeedback) {
        return new FullFocusSessionDTO(
                focusSession.getSessionName(),

                focusSession.getQualityScore(),
                focusSession.getFocusScore(),

                focusSession.getAchievementNote(),
                focusSession.getDistractionNote(),

                aiFeedback.getAiFocusScore(),
                aiFeedback.getAiQualityScore(),
                aiFeedback.getAiNote()
        );
    }
}