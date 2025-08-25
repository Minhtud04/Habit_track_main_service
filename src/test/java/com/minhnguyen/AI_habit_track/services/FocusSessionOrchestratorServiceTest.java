package com.minhnguyen.AI_habit_track.services;

import com.minhnguyen.AI_habit_track.DTO.Internal.AIServiceQueueDTO;
import com.minhnguyen.AI_habit_track.DTO.Request.ActivityRequestDTO;
import com.minhnguyen.AI_habit_track.DTO.Request.FocusSessionRequestDTO;
import com.minhnguyen.AI_habit_track.DTO.UserOAuthDTO;
import com.minhnguyen.AI_habit_track.models.FocusSession;
import com.minhnguyen.AI_habit_track.models.User;
import com.minhnguyen.AI_habit_track.services.sub_services.*;
import com.minhnguyen.AI_habit_track.utils.errorHandler.ErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for FocusSessionOrchestratorService.
 * This test verifies the orchestration logic of the service in isolation,
 * ensuring it calls its dependencies in the correct order.
 */
@ExtendWith(MockitoExtension.class)
class FocusSessionOrchestratorServiceTest {

    // Create mocks for all five dependencies of the orchestrator service
    @Mock
    private FocusSessionService sessionService;
    @Mock
    private ActivityService activityService;
    @Mock
    private QueueService queueService;
    @Mock
    private AuthService authService;
    @Mock
    private UserService userService;

    // Create an instance of the orchestrator and inject the mocks into it
    @InjectMocks
    private FocusSessionOrchestratorService orchestratorService;

    // Declare test data variables to be used across tests
    private FocusSessionRequestDTO requestDTO;
    private UserOAuthDTO userOAuthDTO;
    private User mockUser;
    private FocusSession mockSavedSession;

    @BeforeEach
    void setUp() {

        // mock data 1
        requestDTO = new FocusSessionRequestDTO();
        requestDTO.setSessionName("Afternoon Focus Session 7/18 - Request Flow back ! ");
        requestDTO.setStartTime(1752666000L);
        requestDTO.setEndTime(1752687600L);
        requestDTO.setQualityGrade(4);
        requestDTO.setFocusGrade(4);
        requestDTO.setAchievementNote("1. Deploy AWS lambda for the first time + SQS Queue 2. Integrate Gemini API + ChatGPT API 3. Set up config SAM flow 4. Test around");
        requestDTO.setDistractionNote("Stranger Chat disruption. Youtube disruption while waiting response from AI. Random disruption on phone.");

        List<ActivityRequestDTO> activities = new ArrayList<>();
        activities.add(new ActivityRequestDTO("www.youtube.com", 4200L));
        activities.add(new ActivityRequestDTO("www.gemini.com", 7200L));
        activities.add(new ActivityRequestDTO("www.github.com", 300L));
        activities.add(new ActivityRequestDTO("www.aws.com", 2340L));
        activities.add(new ActivityRequestDTO("www.instagram.com", 2100L));
        requestDTO.setActivities(activities);


        userOAuthDTO = new UserOAuthDTO("MinhTest", "minhtest@example.com");

        // Initialize User
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        // Initialize FocusSession
        mockSavedSession = new FocusSession();
        mockSavedSession.setId(101L);
        mockSavedSession.setUser(mockUser);
    }


    //Basic test case 1: Flow test
    @Test
    @DisplayName("Basic test case 1: Call all sub-services in the correct order")
    void testProcessAndSaveWorkSession_OrchestrationFlow() {

        // auth + user creation
        when(authService.getUserDetails()).thenReturn(userOAuthDTO);
        when(userService.findOrCreateUser(any(UserOAuthDTO.class))).thenReturn(mockUser);

        // session saving and activity saving
        when(sessionService.saveSession(any(FocusSessionRequestDTO.class), any(User.class))).thenReturn(mockSavedSession);
        doNothing().when(activityService).saveAllActivities(any(FocusSessionRequestDTO.class), any(FocusSession.class));

        // queueing for feedback
        doNothing().when(queueService).queueSessionForFeedback(any(AIServiceQueueDTO.class));

        // Complete function test
        FocusSession result = orchestratorService.processAndSaveWorkSession(requestDTO);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(mockSavedSession.getId());

        // Verify flow in-order
        var inOrder = inOrder(authService, userService, sessionService, activityService, queueService);
        inOrder.verify(authService, times(1)).getUserDetails();
        inOrder.verify(userService, times(1)).findOrCreateUser(userOAuthDTO);
        inOrder.verify(sessionService, times(1)).saveSession(requestDTO, mockUser);
        inOrder.verify(activityService, times(1)).saveAllActivities(requestDTO, mockSavedSession);
        inOrder.verify(queueService, times(1)).queueSessionForFeedback(any(AIServiceQueueDTO.class));

        // Queue inspection
        ArgumentCaptor<AIServiceQueueDTO> queueDtoCaptor = ArgumentCaptor.forClass(AIServiceQueueDTO.class);
        verify(queueService).queueSessionForFeedback(queueDtoCaptor.capture());
        assertThat(queueDtoCaptor.getValue().getSessionId()).isEqualTo(101L);
    }


//    // Edge case test 1: Null dto
//    @Test
//    @DisplayName("Edge case test 1: null FocusSessionRequestDTO")
//    void testProcessAndSaveWorkSession_NullRequestDTO() {
//        // --- WHEN / THEN ---
//        assertThatThrownBy(() -> orchestratorService.processAndSaveWorkSession(null))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("FocusSessionRequestDTO cannot be null");
//
//        // Verify no interactions with dependencies
//        verifyNoInteractions(authService, userService, sessionService, activityService, queueService);
//    }

    // Edge case test 2: Unauthenticated
    @Test
    @DisplayName("Edge case test 2: No UserOAuthDTO")
    void testProcessAndSaveWorkSession_AuthenticationFailure() {
        // --- GIVEN ---
        // Simulate authentication failure
        when(authService.getUserDetails()).thenReturn(null);

        // --- WHEN / THEN ---
        assertThatThrownBy(() -> orchestratorService.processAndSaveWorkSession(requestDTO))
                .isInstanceOf(ErrorException.Unauthenticated.class)
                .hasMessageContaining("User is not authenticated or OAuth2 details are missing");

        // Verify no further interactions
        verify(authService, times(1)).getUserDetails();
        verifyNoInteractions(userService, sessionService, activityService, queueService);
    }


    // Edge case test 3: Empty Activities list
    @Test
    @DisplayName("Edge case 3: handle empty activities list")
    void testProcessAndSaveWorkSession_EmptyActivities() {
        // --- GIVEN ---
        // Modify requestDTO to have an empty activities list
        requestDTO.setActivities(new ArrayList<>());

        // Define mock behavior for happy path
        when(authService.getUserDetails()).thenReturn(userOAuthDTO);
        when(userService.findOrCreateUser(any(UserOAuthDTO.class))).thenReturn(mockUser);
        when(sessionService.saveSession(any(FocusSessionRequestDTO.class), any(User.class))).thenReturn(mockSavedSession);
        doNothing().when(queueService).queueSessionForFeedback(any(AIServiceQueueDTO.class));


        FocusSession result = orchestratorService.processAndSaveWorkSession(requestDTO);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(mockSavedSession.getId());

        // Verify interactions: activityService should not be called
        var inOrder = inOrder(authService, userService, sessionService, queueService);
        inOrder.verify(authService, times(1)).getUserDetails();
        inOrder.verify(userService, times(1)).findOrCreateUser(userOAuthDTO);
        inOrder.verify(sessionService, times(1)).saveSession(requestDTO, mockUser);
        inOrder.verify(queueService, times(1)).queueSessionForFeedback(any(AIServiceQueueDTO.class));
        verifyNoInteractions(activityService);

        // Verify AIServiceQueueDTO contents
        ArgumentCaptor<AIServiceQueueDTO> queueDtoCaptor = ArgumentCaptor.forClass(AIServiceQueueDTO.class);
        verify(queueService).queueSessionForFeedback(queueDtoCaptor.capture());
        assertThat(queueDtoCaptor.getValue().getActivities()).isEmpty();
    }
}