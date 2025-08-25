package com.minhnguyen.AI_habit_track.DTO.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minhnguyen.AI_habit_track.DTO.Internal.FullFocusSessionDTO;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class FocusSessionResultResponseDTO {

        @JsonProperty("status_code")
        private Integer status_code;

        @JsonProperty("status")
        private String status; // Pending / Ready

        @JsonProperty("message")
        private String message;

        @JsonProperty("session")
        private FullFocusSessionDTO fullFocusSession;           //Optional?

}
