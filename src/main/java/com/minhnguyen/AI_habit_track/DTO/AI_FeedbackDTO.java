package com.minhnguyen.AI_habit_track.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minhnguyen.AI_habit_track.DTO.ActivityRequestDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object representing the specific information needed
 * for the AI service to generate feedback on a focus session.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AI_FeedbackDTO {

    @NotNull(message = "Start time is required")
    @JsonProperty("start_time")
    private Long startTime; // Unix timestamp in milliseconds

    @NotNull(message = "End time is required")
    @JsonProperty("end_time")
    private Long endTime; // Unix timestamp in milliseconds
    /**
     * A list of activities (domains and usage times) that occurred during the session.
     */
    @NotEmpty(message = "Activities list cannot be empty.")
    @JsonProperty("activities")
    private List<ActivityRequestDTO> activities;

    /**
     * The user's self-reported note on their achievements.
     */
    @JsonProperty("achievement_note")
    private String achievementNote;

    /**
     * The user's self-reported note on what distracted them.
     */
    @JsonProperty("distraction_note")
    private String distractionNote;
}