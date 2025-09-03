package com.minhnguyen.AI_habit_track.DTO.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minhnguyen.AI_habit_track.DTO.Internal.FullFocusSessionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data // Simple DTO, so @Data is acceptable here.
@AllArgsConstructor
public class FocusSessionInstantResponseDTO {
    @JsonProperty("status_code")
    private Integer status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("session_id")
    private Long sessionId;
}
