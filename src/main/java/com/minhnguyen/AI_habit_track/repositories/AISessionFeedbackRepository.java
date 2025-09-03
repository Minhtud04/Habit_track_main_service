package com.minhnguyen.AI_habit_track.repositories;

import com.minhnguyen.AI_habit_track.models.AISessionFeedback;
import com.minhnguyen.AI_habit_track.models.FocusSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AISessionFeedbackRepository extends JpaRepository<AISessionFeedback, Long> {
    AISessionFeedback findByFocusSession(FocusSession focusSession);

}
