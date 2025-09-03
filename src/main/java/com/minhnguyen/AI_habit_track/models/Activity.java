package com.minhnguyen.AI_habit_track.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Duration;
import java.util.Objects;

/**
 * Represents a specific activity performed during a FocusSession.
 * For example, "Coding," "Research," or "Break."
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "focus_session_id", nullable = false) // Corrected typo and made non-nullable
    @ToString.Exclude // Prevents potential infinite loops in toString()
    private FocusSession focusSession;

    @NotBlank(message = "Activity name cannot be blank")
    @Column(nullable = false, length = 255)
    private String activityName;

    @NotNull(message = "Duration cannot be null")
    @Column(nullable = false)
    private Duration duration;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        // Use the primary key for equality checks if it's not null (i.e., for persisted entities)
        return id != null && Objects.equals(id, activity.id);
    }

    @Override
    public int hashCode() {
        // Use a constant for transient entities and the ID's hashcode for persisted ones
        return getClass().hashCode();
    }
}