package com.minhnguyen.AI_habit_track.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Objects;

/**
 * Represents the AI-generated feedback for a specific FocusSession.
 * This entity has a one-to-one relationship with FocusSession and uses
 * a shared primary key strategy, where its ID is the same as the FocusSession's ID.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ai_session_feedback")
public class AISessionFeedback {

    @Id
    // This entity's ID is not auto-generated. It shares the same ID as the
    // FocusSession it belongs to. This is enforced by the @MapsId annotation below.
    @Column(name = "focus_session_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // This annotation links the @Id field (id) to the primary key of the associated FocusSession.
    @JoinColumn(name = "focus_session_id") // Specifies the foreign key column.
    @ToString.Exclude // Prevents infinite loops in toString() calls.
    private FocusSession focusSession;

    @Min(value = 1, message = "AI quality score must be at least 1")
    @Max(value = 10, message = "AI quality score must be at most 10")
    private Integer aiQualityScore;

    @Min(value = 1, message = "AI focus score must be at least 1")
    @Max(value = 10, message = "AI focus score must be at most 10")
    private Integer aiFocusScore;

    @Column(name = "ai_note", length = 2000) // It's good practice to define a length for text fields
    private String aiNote;

    // --- Custom equals() and hashCode() for stable entity management ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AISessionFeedback that = (AISessionFeedback) o;
        // Use the primary key for equality checks if it's not null
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // Use a constant hash code for all instances of this class
        return getClass().hashCode();
    }
}