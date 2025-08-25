//package com.minhnguyen.AI_habit_track.services.sub_services;
//
//import com.minhnguyen.AI_habit_track.DTO.Internal.AIServiceQueueDTO;
//import io.awspring.cloud.sqs.operations.SqsTemplate;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//// Use Mockito with JUnit 5
//@ExtendWith(MockitoExtension.class)
//class QueueServiceTest {
//
//    // Create a mock (a fake version) of the SqsTemplate
//    @Mock
//    private SqsTemplate sqsTemplate;
//
//    // Create an instance of QueueService and inject the mock SqsTemplate into it
//    @InjectMocks
//    private QueueService queueService;
//
//    @Test
//    @DisplayName("queueSessionForFeedback should call SqsTemplate send method once")
//    void testQueueSessionForFeedback_sendsMessage() {
//        // --- GIVEN ---
//        // 1. Since the queueUrl is injected via @Value, we must set it manually for the test.
//        // ReflectionTestUtils is a Spring utility for accessing private fields.
//        ReflectionTestUtils.setField(queueService, "queueUrl", "https://fake-queue.url");
//
//        // 2. Create the DTO payload that will be sent
//        AIServiceQueueDTO feedbackDto = new AIServiceQueueDTO();
//        feedbackDto.setSessionId(101L);
//        feedbackDto.setAchievementNote("Test achievement");
//
//        // --- WHEN ---
//        // 3. Call the method we want to test
//        queueService.queueSessionForFeedback(feedbackDto);
//
//        // --- THEN ---
//        // 4. Verify that the 'send' method of our mock SqsTemplate was called exactly one time.
//        // We use any() because we don't need to inspect the details of the SqsTemplate's internal builder.
//        verify(sqsTemplate, times(1)).send(any());
//    }
//}
