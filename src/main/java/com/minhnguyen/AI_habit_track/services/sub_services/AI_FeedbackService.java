package com.minhnguyen.AI_habit_track.services.sub_services;

import com.minhnguyen.AI_habit_track.DTO.Internal.AI_FeedbackDTO;
import com.minhnguyen.AI_habit_track.models.AISessionFeedback;
import com.minhnguyen.AI_habit_track.models.FocusSession;
import com.minhnguyen.AI_habit_track.repositories.AISessionFeedbackRepository;
import com.minhnguyen.AI_habit_track.repositories.FocusSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AI_FeedbackService {

    private static final Logger LOG = LoggerFactory.getLogger(AI_FeedbackService.class);
    private final AISessionFeedbackRepository aiSessionFeedbackRepository;
    private final FocusSessionRepository focusSessionRepository;

    public AI_FeedbackService(AISessionFeedbackRepository aiSessionFeedbackRepository, FocusSessionRepository focusSessionRepository) {
        this.aiSessionFeedbackRepository = aiSessionFeedbackRepository;
        this.focusSessionRepository = focusSessionRepository;
    }


    /**
     * Creates and saves a new AISessionFeedback entity based on the payload from the AI service.
     *
     * @param focusSessionId The ID of the parent FocusSession.
     * @param payload        The DTO containing the AI-generated scores and notes.
     */
    @Transactional
    public FocusSession saveFeedback(Long focusSessionId, AI_FeedbackDTO payload) {
        LOG.info("Attempting to save AI feedback for FocusSession ID: {}", focusSessionId);

        // 1. Find the parent FocusSession entity.
        FocusSession focusSession = focusSessionRepository.findById(focusSessionId)
                .orElseThrow(() -> new RuntimeException("FocusSession not found with id: " + focusSessionId));


        // 2. Create and populate the new AISessionFeedback entity.
        AISessionFeedback feedback = new AISessionFeedback();
        feedback.setFocusSession(focusSession); // Link it to the parent session.
        feedback.setAiFocusScore(payload.getAiFocusScore());
        feedback.setAiQualityScore(payload.getAiQualityScore());
        feedback.setAiNote(payload.getAiNotes());


        LOG.info("Check existing session succeeded. Saving processing...");
        // 3. Save the new entity to the database.
        aiSessionFeedbackRepository.save(feedback);

        FocusSession updatedSession = focusSessionRepository.findById(focusSessionId)
                .orElseThrow(() -> new RuntimeException("FocusSession not found with id: " + focusSessionId));

        LOG.info("Successfully saved AI feedback ID: {}  - FocusSession ID: {}", updatedSession.getAiSessionFeedback().getId(), updatedSession.getId());

        return updatedSession;
    }


    public AISessionFeedback getAiFeedbackByFocusSession(FocusSession focusSession) {
        LOG.info("Retrieving AI feedback for FocusSession ID: {}", focusSession.getId());

        AISessionFeedback aiSessionFeedback = aiSessionFeedbackRepository.findByFocusSession(focusSession);
        if (aiSessionFeedback == null) {
            LOG.warn("No AI feedback found for FocusSession ID: {}", focusSession.getId());
            return null; // or throw an exception if you prefer
        }
        LOG.info("Successfully retrieved AI feedback ID: {} for FocusSession ID: {}", aiSessionFeedback.getId(), focusSession.getId());
        return aiSessionFeedback;
    }
}
