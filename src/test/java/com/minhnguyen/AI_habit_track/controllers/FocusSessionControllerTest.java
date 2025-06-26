package com.minhnguyen.AI_habit_track.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhnguyen.AI_habit_track.DTO.*;
import com.minhnguyen.AI_habit_track.services.FocusSessionOrchestratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FocusSessionController.class)
@Import(FocusSessionControllerTest.TestConfig.class)
class FocusSessionControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public FocusSessionOrchestratorService focusSessionOrchestratorService() {
            return Mockito.mock(FocusSessionOrchestratorService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FocusSessionOrchestratorService orchestratorService;

    @Autowired
    private ObjectMapper objectMapper;
    private FocusSessionRequestDTO dtoWithEmptyActivities;
    private FocusSessionRequestDTO dtoWithOnlyActivities;
    private FocusSessionRequestDTO dtoWithFullData;
    private FocusSessionRequestDTO dtoBlank;



    @BeforeEach
    void setUp() {
        Mockito.reset(orchestratorService);
        doNothing().when(orchestratorService).processAndSaveWorkSession(any(FocusSessionRequestDTO.class));

        // 1. With Empty Activities and a 1-Hour Duration
        dtoWithEmptyActivities = new FocusSessionRequestDTO();
        dtoWithEmptyActivities.setSessionName("Reviewing Documentation");
        dtoWithEmptyActivities.setStartTime(1719436800000L);
        dtoWithEmptyActivities.setEndTime(1719440400000L);
        dtoWithEmptyActivities.setActivities(Collections.emptyList());
        dtoWithEmptyActivities.setQualityGrade(8);
        dtoWithEmptyActivities.setFocusGrade(7);
        dtoWithEmptyActivities.setAchievementNote("Read through the entire Spring Security documentation.");
        dtoWithEmptyActivities.setDistractionNote("Phone notifications.");

        // 2. With Only Activities (Empty Notes and Scores)
        dtoWithOnlyActivities = new FocusSessionRequestDTO();
        dtoWithOnlyActivities.setSessionName("Coding Session");
        dtoWithOnlyActivities.setStartTime(1719440400000L);
        dtoWithOnlyActivities.setEndTime(1719444000000L);



        dtoWithOnlyActivities.setActivities(Arrays.asList(
                new ActivityRequestDTO("www.github.com", 1800000L),
                new ActivityRequestDTO("stackoverflow.com", 900000L)
        ));
        dtoWithOnlyActivities.setQualityGrade(null);
        dtoWithOnlyActivities.setFocusGrade(null);
        dtoWithOnlyActivities.setAchievementNote("");
        dtoWithOnlyActivities.setDistractionNote("");

        // 3. With Full Data
        dtoWithFullData = new FocusSessionRequestDTO();
        dtoWithFullData.setSessionName("Full Project Work");
        dtoWithFullData.setStartTime(1719444000000L);
        dtoWithFullData.setEndTime(1719447600000L);
        dtoWithFullData.setActivities(Collections.singletonList(new ActivityRequestDTO("ide.mycompany.com", 2400000L)));
        dtoWithFullData.setQualityGrade(9);
        dtoWithFullData.setFocusGrade(6);
        dtoWithFullData.setAchievementNote("Finished feature ticket #123.");
        dtoWithFullData.setDistractionNote("None.");

        // 4. Blank Payload
        dtoBlank = new FocusSessionRequestDTO();
    }

    @Test
    @DisplayName("Test with Full Data should return 200 OK")
    @WithMockUser
    void testWithFullData() throws Exception {
        mockMvc.perform(post("/v1/sessions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoWithFullData)))
                .andExpect(status().isOk());
        verify(orchestratorService).processAndSaveWorkSession(any(FocusSessionRequestDTO.class));
    }

    @Test
    @DisplayName("Test with Empty Activities should return 200 OK")
    @WithMockUser
    void testWithEmptyActivities() throws Exception {
        mockMvc.perform(post("/v1/sessions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoWithEmptyActivities)))
                .andExpect(status().isOk());
        verify(orchestratorService).processAndSaveWorkSession(any(FocusSessionRequestDTO.class));
    }

    @Test
    @DisplayName("Test with Only Activities (null scores) should return 400 - mismatch JSON")
    @WithMockUser
    void testWithOnlyActivities() throws Exception {
        mockMvc.perform(post("/v1/sessions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoWithOnlyActivities)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test with Blank DTO should return 400 Bad Request")
    @WithMockUser
    void testWithBlankData() throws Exception {
        mockMvc.perform(post("/v1/sessions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoBlank)))
                .andExpect(status().isBadRequest()); // Expect validation to fail
    }

    @Test
    @DisplayName("Test with Wrong Score Type (Float) should return 400 Bad Request")
    @WithMockUser
    void testWithFloatScore() throws Exception {
        // This test must use a raw JSON string to simulate the type mismatch.
        String jsonWithFloatScore = "{\"session_name\":\"Invalid Score\"," +
                "\"start_time\":1719447600000," +
                "\"end_time\":1719448200000," +
                "\"quality_grade\":7.5}";

        mockMvc.perform(post("/v1/sessions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithFloatScore))
                .andExpect(status().isBadRequest()); // Expect deserialization to fail
    }
}
