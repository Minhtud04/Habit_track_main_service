package com.minhnguyen.AI_habit_track.repositories;

import org.springframework.stereotype.Repository;
import com.minhnguyen.AI_habit_track.models.AISessionFeedback;

@Repository
public interface AISessionFeedBackRepository {

    AISessionFeedback findByFeedbackId(Long feedbackId);

    AISessionFeedback save(AISessionFeedback feedback);

    AISessionFeedback findBySessionId(Long sessionId);
}
