package com.minhnguyen.AI_habit_track.DTO.AIFlowDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AI_FeedbackResponseDTO {
    @NotNull(message = "credential is required")
    @JsonProperty("email")
    private String email;

    @JsonProperty("AI_focus_score")
    private int aiFocusScore;

    @JsonProperty("AI_quality_score")
    private int aiQualityScore;

    @JsonProperty("AI_notes")
    private String aiNotes;
}
