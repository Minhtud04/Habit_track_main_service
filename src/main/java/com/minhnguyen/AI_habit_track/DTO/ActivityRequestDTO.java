package com.minhnguyen.AI_habit_track.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data // Simple DTO, so @Data is acceptable here.
@AllArgsConstructor
public class ActivityRequestDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("usage_time")
    private long usageTime; // in milliseconds
}