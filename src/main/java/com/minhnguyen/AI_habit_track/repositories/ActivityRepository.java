package com.minhnguyen.AI_habit_track.repositories;

import com.minhnguyen.AI_habit_track.models.Activity;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository {
    Activity findByActivityId(Long activityId);

    Activity save(Activity activity);

    Activity findByUserIdAndDate(Long userId, String activityName);
}
