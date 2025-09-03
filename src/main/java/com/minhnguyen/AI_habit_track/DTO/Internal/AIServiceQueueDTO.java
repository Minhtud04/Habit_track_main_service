package com.minhnguyen.AI_habit_track.DTO.Internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minhnguyen.AI_habit_track.DTO.Request.ActivityRequestDTO;
import com.minhnguyen.AI_habit_track.models.Activity;
import com.minhnguyen.AI_habit_track.models.FocusSession;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing the specific information needed
 * for the AI service to generate feedback on a focus session.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIServiceQueueDTO {
    @NotNull(message = "sessionId is required")
    @JsonProperty("session_id")
    private Long sessionId;

    @NotNull(message = "Start time is required")
    @JsonProperty("start_time")
    private Instant startTime;

    @NotNull(message = "End time is required")
    @JsonProperty("end_time")
    private Instant endTime;


    @NotEmpty(message = "Activities list cannot be empty.")
    @JsonProperty("activities")
    private List<ActivityRequestDTO> activities;


    @JsonProperty("achievement_note")
    private String achievementNote;

    @JsonProperty("distraction_note")
    private String distractionNote;


    public AIServiceQueueDTO(FocusSession session) {
        this.sessionId = session.getId();
        this.startTime = session.getStartTime();
        this.endTime = session.getEndTime();
        this.achievementNote = session.getAchievementNote();
        this.distractionNote = session.getDistractionNote();
        this.activities = new ArrayList<>();
        for (Activity activity : session.getActivities()) {
            this.activities.add(new ActivityRequestDTO(
                    activity.getActivityName(),
                    activity.getDuration())); // Assuming a mapping constructor
        }
    }
}