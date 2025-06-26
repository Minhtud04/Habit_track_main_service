package com.minhnguyen.AI_habit_track.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a single focus session tracked by a user.
 * Each session records the start and end times, user-assessed scores,
 * and notes about achievements and distractions.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "focus_sessions")
public class FocusSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Session name != null")
    private String sessionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @NotNull(message = "Start date and time cannot be null")
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @NotNull(message = "End date and time cannot be null")
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Min(value = 1, message = "Quality score min = 1")
    @Max(value = 10, message = "Quality score max = 10")
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 5")
    private Integer qualityScore;

    @Min(value = 1, message = "Focus score must be at least 1")
    @Max(value = 10, message = "Focus score must be at most 10")
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 5")
    private Integer focusScore;

    @Column(length = 1000) // It's good practice to define a length for text fields
    private String achievementNote;

    @Column(length = 1000)
    private String distractionNote;

    // The 'fetch' strategy is LAZY by default for @OneToOne on the non-owning side.
    // 'mappedBy' indicates that the AISessionFeedback entity is responsible for the foreign key relationship.
    @OneToOne(mappedBy = "focusSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Exclude from toString to prevent potential infinite loops
    private AISessionFeedback aiSessionFeedback;

    // Helper method to associate feedback with this session
    public void setAiSessionFeedback(AISessionFeedback feedback) {
        if (feedback != null) {
            feedback.setFocusSession(this);
        }
        this.aiSessionFeedback = feedback;
    }

    // --- Custom equals() and hashCode() for stable entity management ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FocusSession that = (FocusSession) o;
        // Use the primary key for equality checks if it's not null
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // Use a constant for transient entities and the ID's hashcode for persisted ones
        return getClass().hashCode();
    }
}