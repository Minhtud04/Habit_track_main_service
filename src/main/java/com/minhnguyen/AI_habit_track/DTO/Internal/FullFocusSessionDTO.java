package com.minhnguyen.AI_habit_track.DTO.Internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class FullFocusSessionDTO {
    @JsonProperty("session_name")
    private String sessionName; // This field isn't in your JPA model, can be used for logging or ignored.

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

    @JsonProperty("AI_focus_score")
    private int aiFocusScore;

    @JsonProperty("AI_quality_score")
    private int aiQualityScore;

    @JsonProperty("AI_notes")
    private String aiNotes;
}
