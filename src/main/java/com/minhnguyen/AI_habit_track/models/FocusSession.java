package com.minhnguyen.AI_habit_track.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
    public enum AiFeedbackStatus {
        NO,
        YES,
        PENDING
    }

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
    private Instant startTime;

    @NotNull(message = "End date and time cannot be null")          //application level
    @Column(nullable = false)                                       //Database JPA level
    private Instant endTime;

    @OneToMany(mappedBy = "focusSession")
    private List<Activity> activities =  new ArrayList<>();;

    @Min(value = 1, message = "Quality score min = 1")
    @Max(value = 10, message = "Quality score max = 10")
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 5")
    private Integer qualityScore;

    @Min(value = 1, message = "Focus score must be at least 1")
    @Max(value = 10, message = "Focus score must be at most 10")
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 5")
    private Integer focusScore;

    @Column(length = 1200)  //~200 words
    private String achievementNote;

    @Column(length = 1200)  //~200 words
    private String distractionNote;

    // The 'fetch' strategy is LAZY by default for @OneToOne on the non-owning side.
    @OneToOne(mappedBy = "focusSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private AISessionFeedback aiSessionFeedback;

    //Status
    @NotNull
    @Enumerated(EnumType.STRING) // This is important!
    private AiFeedbackStatus aiFeedbackStatus;

    //Time-limit for aiFeedback Retry
    @NotNull(message = "Time limit cannot be null")
    @Column(nullable = false)
    private Instant processExceededTime;


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