package com.minhnguyen.AI_habit_track.repositories;

import com.minhnguyen.AI_habit_track.models.AISessionFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AISessionFeedbackRepository extends JpaRepository<AISessionFeedback, Long> { // <-- Change FocusSession to AISessionFeedback


}
