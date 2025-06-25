package com.minhnguyen.AI_habit_track.repositories;

import com.minhnguyen.AI_habit_track.models.FocusSession;
import org.springframework.stereotype.Repository;

@Repository
public interface FocusSessionRepository {

    FocusSession findBySessionId(Long sessionId);
    FocusSession save(FocusSession session);

    FocusSession findByUserIdAndDate(Long userId, String date);
}
