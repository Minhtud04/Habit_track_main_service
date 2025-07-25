package com.minhnguyen.AI_habit_track.services.sub_services;

import com.minhnguyen.AI_habit_track.DTO.ActivitiesFlowDTO.FocusSessionRequestDTO;
import com.minhnguyen.AI_habit_track.models.Activity;
import com.minhnguyen.AI_habit_track.models.FocusSession;
import com.minhnguyen.AI_habit_track.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void saveAllActivities(FocusSessionRequestDTO focusSessionRequestDTO, FocusSession savedSession) {
        List<Activity> activities = mapDtoToActivities(focusSessionRequestDTO, savedSession);
        activityRepository.saveAll(activities);
    }

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
}