package com.minhnguyen.AI_habit_track.DTO.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minhnguyen.AI_habit_track.DTO.Request.ActivityRequestDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class FocusSessionRequestDTO {

    @JsonProperty("session_name")
    private String sessionName; // This field isn't in your JPA model, can be used for logging or ignored.

    @NotNull(message = "Start time is required")
    @JsonProperty("start_time")
    private Instant startTime; // Represents an exact moment in UTC

    @NotNull(message = "End time is required")
    @JsonProperty("end_time")
    private Instant endTime; // Represents an exact moment in UTC

    @JsonProperty("activities")
    private List<ActivityRequestDTO> activities;

    @NotNull
    @Min(1) @Max(10)
    @JsonProperty("quality_grade")
    private Integer qualityGrade;

    @NotNull
    @Min(1) @Max(10)
    @JsonProperty("focus_grade")
    private Integer focusGrade;

    @JsonProperty("achievement_note")
    private String achievementNote;

    @JsonProperty("distraction_note")
    private String distractionNote;
}