package com.minhnguyen.AI_habit_track.services.sub_services;

import com.minhnguyen.AI_habit_track.DTO.Request.FocusSessionRequestDTO;
import com.minhnguyen.AI_habit_track.models.FocusSession;
import com.minhnguyen.AI_habit_track.models.User;
import com.minhnguyen.AI_habit_track.repositories.FocusSessionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class FocusSessionService {
    private final FocusSessionRepository focusSessionRepository;

    public FocusSessionService(FocusSessionRepository focusSessionRepository) {
        this.focusSessionRepository = focusSessionRepository;
    }

    public FocusSession saveSession(FocusSessionRequestDTO focusSessionRequestDTO, User user) {
        FocusSession session = mapDtoToFocusSession(focusSessionRequestDTO, user);
        return focusSessionRepository.save(session);
    }

    public FocusSession getFocusSessionById(Long sessionId) {
        return focusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Focus session not found with ID: " + sessionId));
    }


    private FocusSession mapDtoToFocusSession(FocusSessionRequestDTO dto, User user) {
        FocusSession session = new FocusSession();

        session.setSessionName(dto.getSessionName() != null ? dto.getSessionName() : "Focus Session");
        session.setStartDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getStartTime()), ZoneId.systemDefault()));
        session.setEndDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getEndTime()), ZoneId.systemDefault()));
        session.setQualityScore(dto.getQualityGrade());
        session.setFocusScore(dto.getFocusGrade());
        session.setAchievementNote(dto.getAchievementNote());
        session.setDistractionNote(dto.getDistractionNote());
        session.setUser(user);

        return session;
    }
}