package com.minhnguyen.AI_habit_track.services;

import com.minhnguyen.AI_habit_track.DTO.*;
import com.minhnguyen.AI_habit_track.DTO.AIFlowDTO.AIServiceQueueDTO;
import com.minhnguyen.AI_habit_track.DTO.ActivitiesFlowDTO.FocusSessionRequestDTO;
import com.minhnguyen.AI_habit_track.controllers.FocusSessionController;
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
public class FocusSessionOrchestratorService {
    private static final Logger logger = LoggerFactory.getLogger(FocusSessionController.class);
    private final FocusSessionService sessionService;
    private final ActivityService activityService;
    private final QueueService queueService;
    private final AuthService authService;
    private final UserService userService;

    public FocusSessionOrchestratorService(UserService userService, AuthService authService, FocusSessionService sessionService, ActivityService activityService, QueueService queueService) {
        this.sessionService = sessionService;
        this.activityService = activityService;
        this.queueService = queueService;
        this.authService = authService;
        this.userService = userService;
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
        queueService.queueSessionForFeedback(createAiFeedbackDto(focusSessionRequestDTO, savedSession));
        logger.info("Successfully saved and sent to Queue. Focus Session ID: {} - User email: {}", savedSession.getId(), savedSession.getUser().getEmail()); // Log the session ID for tracking

        return savedSession;
    }


    private User getAuthenticatedUser() {
        UserOAuthDTO userOAuthDTO = authService.getUserDetails();
        if (userOAuthDTO == null) {
            throw new ErrorException.Unauthenticated("User is not authenticated or OAuth2 details are missing.");
        }
        return userService.findOrCreateUser(userOAuthDTO);
    }


    private AIServiceQueueDTO createAiFeedbackDto(FocusSessionRequestDTO requestDto, FocusSession savedSession) {
        return new AIServiceQueueDTO(
                savedSession.getId(),
                requestDto.getStartTime(),
                requestDto.getEndTime(),
                requestDto.getActivities(),
                requestDto.getAchievementNote(),
                requestDto.getDistractionNote()
        );
    }
}