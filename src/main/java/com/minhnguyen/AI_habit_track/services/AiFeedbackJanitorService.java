package com.minhnguyen.AI_habit_track.services;

import com.minhnguyen.AI_habit_track.DTO.Internal.AIServiceQueueDTO;
import com.minhnguyen.AI_habit_track.models.FocusSession;
import com.minhnguyen.AI_habit_track.repositories.FocusSessionRepository;
import com.minhnguyen.AI_habit_track.services.sub_services.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class AiFeedbackJanitorService {
    private static final Logger logger = LoggerFactory.getLogger(AiFeedbackJanitorService.class);
    private final FocusSessionRepository focusSessionRepository;

    private final QueueService queueService;

    public AiFeedbackJanitorService(FocusSessionRepository focusSessionRepository, QueueService queueService) {
        this.focusSessionRepository = focusSessionRepository;
        this.queueService = queueService;
    }

    /**
     * This method runs on a fixed schedule (e.g., every 5 minutes) to find and handle
     * focus sessions that are stuck in a PENDING state.
     */
    @Scheduled(fixedRateString = "${janitor.feedback.run.interval:300000}") // Default: 5 minutes
    @Transactional
    public void findAndHandleStaleSessions() {
        Instant currentTime = Instant.now();
        logger.info("Running janitor job at {} to find stale feedback sessions.", currentTime);

        // Find sessions that are PENDING and have exceeded their processing time
        List<FocusSession> staleSessions = focusSessionRepository
                .findByAiFeedbackStatusAndProcessExceededTimeBefore(
                        FocusSession.AiFeedbackStatus.PENDING,
                        Instant.now()
                );

        if (staleSessions.isEmpty()) {
            logger.info("No stale sessions found.");
            return;
        }

        logger.warn("Found {} stale session(s). Retry !", staleSessions.size());
        for (FocusSession session : staleSessions) {
            logger.warn("  - Stale FocusSession ID: {}. Exceeded time was: {}",
                    session.getId(), session.getProcessExceededTime());

            handleStaleSession(session);
        }
    }


    private void handleStaleSession(FocusSession session) {
        logger.info("Retry session Id {}", session.getId());
        AIServiceQueueDTO queueRequest = new AIServiceQueueDTO(session);
        queueService.queueSessionForFeedback(queueRequest);
        session.setAiFeedbackStatus(FocusSession.AiFeedbackStatus.PENDING);
        session.setProcessExceededTime(Instant.now().plus(2, ChronoUnit.MINUTES));
    }
}