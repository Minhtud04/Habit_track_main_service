package com.minhnguyen.AI_habit_track.DTO.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

@Data // Simple DTO, so @Data is acceptable here.
@AllArgsConstructor
public class ActivityRequestDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("usage_time")
    private Duration usageTime;
}