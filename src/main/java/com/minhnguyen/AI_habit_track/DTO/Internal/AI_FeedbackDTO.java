package com.minhnguyen.AI_habit_track.DTO.Internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AI_FeedbackDTO {
    @NotNull(message = "sessionId is required")
    @JsonProperty("session_id")
    private Long sessionId;

    @JsonProperty("AI_focus_score")
    private int aiFocusScore;

    @JsonProperty("AI_quality_score")
    private int aiQualityScore;

    @JsonProperty("AI_notes")
    private String aiNotes;
}
