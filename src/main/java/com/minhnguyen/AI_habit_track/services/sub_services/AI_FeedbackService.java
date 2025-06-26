package com.minhnguyen.AI_habit_track.services.sub_services;

import com.minhnguyen.AI_habit_track.DTO.AI_FeedbackDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AI_FeedbackService {
    private static final Logger logger = LoggerFactory.getLogger(AI_FeedbackService.class);

    // In a real app, you would inject RabbitTemplate, KafkaTemplate, etc.

    /**
     * Sends a structured DTO to a message queue for asynchronous AI processing.
     *
     * @param sessionId The ID of the persisted FocusSession this feedback relates to.
     * @param feedbackDto The DTO containing all data needed for the AI.
     */
    public void queueSessionForFeedback(Long sessionId, AI_FeedbackDTO feedbackDto) {
        // The payload for the message queue now neatly contains the session ID and the feedback data.
        var payload = new AIQueuePayload(sessionId, feedbackDto);

        // In a real app, you would serialize and send this 'payload' object.
        // e.g., rabbitTemplate.convertAndSend("ai-feedback-exchange", "ai.feedback.request", payload);

        logger.info("Queuing session for AI feedback. Payload: {}", payload);
    }

    // A private record to represent the message payload. It's clean and immutable.
    private record AIQueuePayload(Long sessionId, AI_FeedbackDTO feedbackData) {}
}