package com.minhnguyen.AI_habit_track.repositories;

import com.minhnguyen.AI_habit_track.models.FocusSession;
import com.minhnguyen.AI_habit_track.models.User; // Make sure User is imported
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// The @Repository annotation is actually optional here, as extending JpaRepository is enough for Spring to find it.
// But it's good practice to keep it for clarity.
@Repository
public interface FocusSessionRepository extends JpaRepository<FocusSession, Long> {
    /**
     * Finds all focus sessions for a specific user that started within a given time range.
     * The method name is parsed by Spring Data JPA to create the query automatically:
     * findBy -> The query prefix
     * User_Id -> Looks for a field named 'user' and finds its 'id' property.
     * And -> Combines criteria
     * StartDateTimeBetween -> Looks for the 'startDateTime' field and applies a 'BETWEEN' clause.
     *
     * @param userId The ID of the User to search for.
     * @param start The start of the date/time range (inclusive).
     * @param end The end of the date/time range (inclusive).
     * @return A list of matching focus sessions.
     */
    List<FocusSession> findByUserIdAndStartDateTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);

    // If you want to find all sessions for a specific user, it would simply be:
    List<FocusSession> findByUser(User user);
    List<FocusSession> findAllByUserAndEndDateTime(User user, LocalDateTime endDateTime);
}