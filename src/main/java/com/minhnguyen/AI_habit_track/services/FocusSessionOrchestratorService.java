package com.minhnguyen.AI_habit_track.services;

import com.minhnguyen.AI_habit_track.DTO.*;
import com.minhnguyen.AI_habit_track.models.*;
import com.minhnguyen.AI_habit_track.services.sub_services.*;
import com.minhnguyen.AI_habit_track.utils.Log.Logger;
import com.minhnguyen.AI_habit_track.utils.errorHandler.ErrorException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FocusSessionOrchestratorService {
    private final Logger logger = new Logger("Orchestrator Service");
    private final FocusSessionService sessionService;
    private final ActivityService activityService;
    private final AI_FeedbackService aiService;
    private final AuthService authService;
    private final UserService userService;

    public FocusSessionOrchestratorService(UserService userService, AuthService authService, FocusSessionService sessionService, ActivityService activityService, AI_FeedbackService aiService) {
        this.sessionService = sessionService;
        this.activityService = activityService;
        this.aiService = aiService;
        this.authService = authService;
        this.userService = userService;
    }

    @Transactional
    public void processAndSaveWorkSession(FocusSessionRequestDTO dto) {
        logger.log("Processing dto");

        User user = getAuthenticatedUser();

        FocusSession newSession = mapDtoToFocusSession(dto, user);
        FocusSession savedSession = sessionService.saveSession(newSession);

        if (dto.getActivities() != null && !dto.getActivities().isEmpty()) {
            List<Activity> activities = mapDtoToActivities(dto, savedSession);
            activityService.saveAllActivities(activities);
        }

        // 4. Send the data to the message queue for AI processing
        aiService.queueSessionForFeedback(savedSession.getId(), createAiFeedbackDto(dto));
    }


    private User getAuthenticatedUser() {
        //log
        logger.log("Retrieving authenticated user details.");

        UserOAuthDTO userOAuthDTO = authService.getUserDetails();
        if (userOAuthDTO == null) {
            throw new ErrorException.Unauthenticated("User is not authenticated or OAuth2 details are missing.");
        }
        return userService.findOrCreateUser(userOAuthDTO);
    }

    private FocusSession mapDtoToFocusSession(FocusSessionRequestDTO dto, User user) {
        logger.log("Mapping DTO to FocusSession entity.");

        FocusSession session = new FocusSession();

        session.setSessionName(dto.getSessionName() != null ? dto.getSessionName() : "Focus Session");

        session.setStartDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getStartTime()), ZoneId.systemDefault()));
        session.setEndDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getEndTime()), ZoneId.systemDefault()));

        session.setQualityScore(dto.getQualityGrade());
        session.setFocusScore(dto.getFocusGrade());

        session.setAchievementNote(dto.getAchievementNote());
        session.setDistractionNote(dto.getDistractionNote());

        session.setUser(user);
        return session;
    }

    // mapDtoToActivities method remains the same
    private List<Activity> mapDtoToActivities(FocusSessionRequestDTO dto, FocusSession parentSession) {
        return dto.getActivities().stream()
                .map(activityDto -> {
                    Activity activity = new Activity();
                    activity.setActivityName(activityDto.getName());
                    activity.setDuration(Duration.ofMillis(activityDto.getUsageTime()));
                    activity.setFocusSession(parentSession);
                    return activity;
                })
                .collect(Collectors.toList());
    }

    /**
     * Creates the data transfer object specifically for the AI service.
     * This decouples the AI service from the main request DTO.
     * @param requestDto The original request DTO from the client.
     * @return A new AI_FeedbackDTO instance.
     */
    private AI_FeedbackDTO createAiFeedbackDto(FocusSessionRequestDTO requestDto) {
        return new AI_FeedbackDTO(
                requestDto.getStartTime(),
                requestDto.getEndTime(),
                requestDto.getActivities(),
                requestDto.getAchievementNote(),
                requestDto.getDistractionNote()
        );
    }
}