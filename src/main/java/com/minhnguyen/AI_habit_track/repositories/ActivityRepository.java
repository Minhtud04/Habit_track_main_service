package com.minhnguyen.AI_habit_track.repositories;

import com.minhnguyen.AI_habit_track.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// The @Repository annotation is optional when extending JpaRepository, but it's good for clarity.
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    /**
     * Finds all activities associated with a specific FocusSession ID.
     * The method name "findByFocusSessionId" tells Spring Data JPA to look for
     * a field named 'focusSession' in the Activity entity and then find its 'id' property.
     *
     * @param sessionId The ID of the FocusSession.
     * @return A list of all activities for that session.
     */
    List<Activity> findByFocusSessionId(Long sessionId);
}