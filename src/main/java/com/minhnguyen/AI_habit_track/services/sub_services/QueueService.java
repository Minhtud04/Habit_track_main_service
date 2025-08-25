package com.minhnguyen.AI_habit_track.services.sub_services;

import com.minhnguyen.AI_habit_track.DTO.Internal.AIServiceQueueDTO;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class QueueService {
    private static final Logger LOG = LoggerFactory.getLogger(AI_FeedbackService.class);

    @Value("${app.sqs.queue-url}") // Injects the queue URL from application.yml
    private String queueUrl;

    private final SqsTemplate sqsTemplate;
    public QueueService(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }


    /**
     * Serializes the session data and sends it to an SQS queue for asynchronous AI processing.
     * @param feedbackDto The data payload for the AI service.
     */
    public void queueSessionForFeedback(AIServiceQueueDTO feedbackDto) {
        try {
            LOG.info("Queuing session for AI feedback. Payload: {}", feedbackDto);
            sqsTemplate.send(to -> to.queue(queueUrl).payload(feedbackDto));
            LOG.info("Successfully sent message for session ID: {} to SQS queue.", feedbackDto.getSessionId());
        } catch (Exception e) {
            LOG.error("Failed to send message to SQS for session ID: {}", feedbackDto.getSessionId(), e);
        }
    }
}
