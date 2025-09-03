//package com.minhnguyen.AI_habit_track.services;
//
//import com.minhnguyen.AI_habit_track.DTO.Internal.AI_FeedbackDTO;
//import com.minhnguyen.AI_habit_track.DTO.AIFlowDTO.AI_FeedbackResponseDTO;
//import com.minhnguyen.AI_habit_track.models.FocusSession;
//import com.minhnguyen.AI_habit_track.models.User;
//import com.minhnguyen.AI_habit_track.services.sub_services.AI_FeedbackService;
//import com.minhnguyen.AI_habit_track.services.sub_services.FocusSessionService;
//import com.minhnguyen.AI_habit_track.services.sub_services.NotificationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
///**
// * Unit test for AIFeedbackOrchestratorService.
// * This test verifies the orchestration logic of the service in isolation,
// * ensuring it calls its dependencies in the correct order and handles edge cases.
// * Note: AuthService is not used as this is an internal route with no authentication check.
// */
//@ExtendWith(MockitoExtension.class)
//class AIFeedbackOrchestratorServiceTest {
//
//    // Create mocks for the three relevant dependencies of the orchestrator service
//    @Mock
//    private FocusSessionService sessionService;
//    @Mock
//    private AI_FeedbackService aiFeedbackService;
//    @Mock
//    private NotificationService notificationService;
//
//    // Create an instance of the orchestrator and inject the mocks into it
//    @InjectMocks
//    private AIFeedbackOrchestratorService orchestratorService;
//
//    // Declare test data variables to be used across tests
//    private AI_FeedbackDTO feedbackDTO;
//    private User mockUser;
//    private FocusSession mockSession;
//
//    @BeforeEach
//    void setUp() {
//        // --- GIVEN ---
//        // Prepare all the mock data needed for the test before each run
//        feedbackDTO = new AI_FeedbackDTO();
//        feedbackDTO.setSessionId(101L);
//        feedbackDTO.setAiFocusScore(4); // Aligned with JSON quality_grade
//        feedbackDTO.setAiQualityScore(4); // Aligned with JSON focus_grade
//        feedbackDTO.setAiNotes("AI analysis: High social media usage (Instagram: 2100s) detected. Suggest reducing distractions to improve focus.");
//
//        mockUser = new User();
//        mockUser.setId(1L);
//        mockUser.setEmail("test@example.com");
//
//        mockSession = new FocusSession();
//        mockSession.setId(101L);
//        mockSession.setUser(mockUser);
//    }
//
//    @Test
//    @DisplayName("saveAiFeedbackAndNotifyUser should call services in correct order and send notification")
//    void testSaveAiFeedbackAndNotifyUser_Success() {
//        // --- GIVEN ---
//        // Define mock behavior for happy path
//        when(aiFeedbackService.saveFeedback(any(Long.class), any(AI_FeedbackDTO.class))).thenReturn(mockSession);
//        doNothing().when(notificationService).sendFeedbackUpdate(any(AI_FeedbackResponseDTO.class));
//
//        // --- WHEN ---
//        orchestratorService.saveAiFeedbackAndNotifyUser(feedbackDTO);
//
//        // --- THEN ---
//        // Verify that each service method was called exactly once in the expected sequence
//        var inOrder = inOrder(aiFeedbackService, notificationService);
//        inOrder.verify(aiFeedbackService, times(1)).saveFeedback(feedbackDTO.getSessionId(), feedbackDTO);
//        inOrder.verify(notificationService, times(1)).sendFeedbackUpdate(any(AI_FeedbackResponseDTO.class));
//
//        // Verify the AI_FeedbackResponseDTO passed to notificationService
//        ArgumentCaptor<AI_FeedbackResponseDTO> responseCaptor = ArgumentCaptor.forClass(AI_FeedbackResponseDTO.class);
//        verify(notificationService).sendFeedbackUpdate(responseCaptor.capture());
//        AI_FeedbackResponseDTO capturedResponse = responseCaptor.getValue();
//        assertThat(capturedResponse.getEmail()).isEqualTo("test@example.com");
//        assertThat(capturedResponse.getAiFocusScore()).isEqualTo(4);
//        assertThat(capturedResponse.getAiQualityScore()).isEqualTo(4);
//        assertThat(capturedResponse.getAiNotes()).isEqualTo("AI analysis: High social media usage (Instagram: 2100s) detected. Suggest reducing distractions to improve focus.");
//
//        // Verify no interactions with unused dependencies
//        verifyNoInteractions(sessionService);
//    }
//
//    @Test
//    @DisplayName("saveAiFeedbackAndNotifyUser should throw IllegalArgumentException for null AI_FeedbackDTO")
//    void testSaveAiFeedbackAndNotifyUser_NullFeedbackDTO() {
//        // --- WHEN / THEN ---
//        assertThatThrownBy(() -> orchestratorService.saveAiFeedbackAndNotifyUser(null))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("AI_FeedbackDTO cannot be null");
//
//        verifyNoInteractions(aiFeedbackService, notificationService, sessionService);
//    }
//
//    @Test
//    @DisplayName("saveAiFeedbackAndNotifyUser should log error for null session")
//    void testSaveAiFeedbackAndNotifyUser_NullSession() {
//        // --- GIVEN ---
//        // Simulate aiFeedbackService returning null
//        when(aiFeedbackService.saveFeedback(any(Long.class), any(AI_FeedbackDTO.class))).thenReturn(null);
//
//        // --- WHEN ---
//        orchestratorService.saveAiFeedbackAndNotifyUser(feedbackDTO);
//
//        // --- THEN ---
//        // Verify aiFeedbackService was called, but notificationService was not
//        verify(aiFeedbackService, times(1)).saveFeedback(feedbackDTO.getSessionId(), feedbackDTO);
//        verifyNoInteractions(notificationService, sessionService);
//    }
//
//    @Test
//    @DisplayName("saveAiFeedbackAndNotifyUser should log error for session with null user")
//    void testSaveAiFeedbackAndNotifyUser_SessionWithNullUser() {
//        // --- GIVEN ---
//        // Simulate session with null user
//        FocusSession sessionWithNullUser = new FocusSession();
//        sessionWithNullUser.setId(101L);
//        sessionWithNullUser.setUser(null);
//        when(aiFeedbackService.saveFeedback(any(Long.class), any(AI_FeedbackDTO.class))).thenReturn(sessionWithNullUser);
//
//        // --- WHEN ---
//        orchestrator