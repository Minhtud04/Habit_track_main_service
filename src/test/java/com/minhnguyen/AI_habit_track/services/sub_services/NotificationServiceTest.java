//package com.minhnguyen.AI_habit_track.services.sub_services;
//
//import ch.qos.logback.classic.Logger;
//import ch.qos.logback.classic.spi.ILoggingEvent;
//import ch.qos.logback.core.read.ListAppender;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.minhnguyen.AI_habit_track.DTO.AIFlowDTO.AI_FeedbackResponseDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//
//import java.io.IOException;
//import java.security.Principal;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
///**
// * Unit test for NotificationService.
// * Verifies WebSocket connection handling and feedback message sending in isolation.
// */
//@ExtendWith(MockitoExtension.class)
//class NotificationServiceTest {
//
//    // Create mocks for dependencies
//    @Mock
//    private WebSocketSession session;
//    @Mock
//    private OAuth2User oAuth2User;
//    @Mock
//    private ObjectMapper objectMapper;
//
//    // Create an instance of the service and inject mocks
//    @InjectMocks
//    private NotificationService notificationService;
//
//    // Declare test data variables
//    private AI_FeedbackResponseDTO feedbackDTO;
//    private String userEmail;
//    private ListAppender<ILoggingEvent> logAppender;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize test data
//        userEmail = "test@example.com";
//        feedbackDTO = new AI_FeedbackResponseDTO();
//        feedbackDTO.setEmail(userEmail);
//        feedbackDTO.setAiFocusScore(4); // From JSON focus_grade
//        feedbackDTO.setAiQualityScore(4); // From JSON quality_grade
//        feedbackDTO.setAiNotes("AI analysis: High social media usage (Instagram: 2100s) detected. Suggest reducing distractions to improve focus.");
//
//        // Setup mock OAuth2User implementing both OAuth2User and Principal
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("email", userEmail);
//        oAuth2User = new DefaultOAuth2User(
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
//                attributes,
//                "email"
//        );
//        when(session.getPrincipal()).thenReturn(oAuth2User);
//
//        // Setup log capture
//        logAppender = new ListAppender<>();
//        logAppender.start();
//        Logger logger = (Logger) LoggerFactory.getLogger(NotificationService.class);
//        logger.addAppender(logAppender);
//    }
//
//    @Test
//    @DisplayName("afterConnectionEstablished should store session for authenticated user")
//    void testAfterConnectionEstablished_AuthenticatedUser() throws Exception {
//
//        when(session.isOpen()).thenReturn(true);
//        notificationService.afterConnectionEstablished(session);
//        assertThat(notificationService.getSessions()).containsKey(userEmail);
//        assertThat(notificationService.getSessions().get(userEmail)).isEqualTo(session);
//
//        // Verify log
//        assertThat(logAppender.list).anyMatch(event ->
//                event.getFormattedMessage().contains("WebSocket connection established for user: " + userEmail));
//    }
////
////    @Test
////    @DisplayName("afterConnectionEstablished should close session for unauthenticated user")
////    void testAfterConnectionEstablished_UnauthenticatedUser() throws Exception {
////        // --- GIVEN ---
////        when(session.getPrincipal()).thenReturn(null);
////
////        // --- WHEN ---
////        notificationService.afterConnectionEstablished(session);
////
////        // --- THEN ---
////        // Verify session is closed
////        verify(session, times(1)).close(eq(CloseStatus.POLICY_VIOLATION.withReason("User must be an authenticated OAuth2 user")));
////
////        // Verify no session is stored
////        assertThat(notificationService.getSessions()).isEmpty();
////
////        // Verify log
////        assertThat(logAppender.list).anyMatch(event ->
////                event.getFormattedMessage().contains("WebSocket connection attempted without an authenticated OAuth2 user. Closing session."));
////    }
////
////    @Test
////    @DisplayName("afterConnectionClosed should remove session for authenticated user")
////    void testAfterConnectionClosed_AuthenticatedUser() throws Exception {
////        // --- GIVEN ---
////        // Simulate an existing session
////        notificationService.getSessions().put(userEmail, session);
////
////        // --- WHEN ---
////        notificationService.afterConnectionClosed(session, CloseStatus.NORMAL);
////
////        // --- THEN ---
////        // Verify session is removed
////        assertThat(notificationService.getSessions()).doesNotContainKey(userEmail);
////
////        // Verify log
////        assertThat(logAppender.list).anyMatch(event ->
////                event.getFormattedMessage().contains("WebSocket connection closed for user: " + userEmail + ". Status: " + CloseStatus.NORMAL));
////    }
////
////    @Test
////    @DisplayName("sendFeedbackUpdate should send message for open session")
////    void testSendFeedbackUpdate_Success() throws Exception {
////        // --- GIVEN ---
////        when(session.isOpen()).thenReturn(true);
////        when(objectMapper.writeValueAsString(feedbackDTO)).thenReturn("{\"email\":\"test@example.com\",\"aiFocusScore\":4,\"aiQualityScore\":4,\"aiNotes\":\"AI analysis: High social media usage (Instagram: 2100s) detected. Suggest reducing distractions to improve focus.\"}");
////        notificationService.getSessions().put(userEmail, session);
////
////        // --- WHEN ---
////        notificationService.sendFeedbackUpdate(feedbackDTO);
////
////        // --- THEN ---
////        // Verify message is sent
////        verify(objectMapper, times(1)).writeValueAsString(feedbackDTO);
////        verify(session, times(1)).sendMessage(eq(new TextMessage("{\"email\":\"test@example.com\",\"aiFocusScore\":4,\"aiQualityScore\":4,\"aiNotes\":\"AI analysis: High social media usage (Instagram: 2100s) detected. Suggest reducing distractions to improve focus.\"}")));
////
////        // Verify log
////        assertThat(logAppender.list).anyMatch(event ->
////                event.getFormattedMessage().contains("Successfully sent ai feedback to user: " + userEmail));
////    }
////
////    @Test
////    @DisplayName("sendFeedbackUpdate should log warning for no active session")
////    void testSendFeedbackUpdate_NoSession() {
////        // --- GIVEN ---
////        // No session in the map for userEmail
////
////        // --- WHEN ---
////        notificationService.sendFeedbackUpdate(feedbackDTO);
////
////        // --- THEN ---
////        // Verify no interactions with session or ObjectMapper
////        verifyNoInteractions(session, objectMapper);
////
////        // Verify log
////        assertThat(logAppender.list).anyMatch(event ->
////                event.getFormattedMessage().contains("No active WebSocket session found for user: " + userEmail + ". Cannot send update."));
////    }
////
////    @Test
////    @DisplayName("sendFeedbackUpdate should log error for serialization failure")
////    void testSendFeedbackUpdate_SerializationFailure() throws Exception {
////        // --- GIVEN ---
////        when(session.isOpen()).thenReturn(true);
////        when(objectMapper.writeValueAsString(feedbackDTO)).thenThrow(new IOException("Serialization error"));
////        notificationService.getSessions().put(userEmail, session);
////
////        // --- WHEN ---
////        notificationService.sendFeedbackUpdate(feedbackDTO);
////
////        // --- THEN ---
////        // Verify ObjectMapper was called, but no message was sent
////        verify(objectMapper, times(1)).writeValueAsString(feedbackDTO);
////        verify(session, never()).sendMessage(any(TextMessage.class));
////
////        // Verify log
////        assertThat(logAppender.list).anyMatch(event ->
////                event.getFormattedMessage().contains("Failed to send WebSocket message to user: " + userEmail));
////    }
//}