package com.minhnguyen.AI_habit_track.services.sub_services;

import com.minhnguyen.AI_habit_track.models.Activity;
import com.minhnguyen.AI_habit_track.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void saveAllActivities(List<Activity> activities) {
        activityRepository.saveAll(activities);
    }
}