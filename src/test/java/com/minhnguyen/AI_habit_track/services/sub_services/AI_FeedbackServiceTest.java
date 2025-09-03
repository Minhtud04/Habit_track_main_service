package com.minhnguyen.AI_habit_track.services.sub_services;

import com.minhnguyen.AI_habit_track.DTO.Internal.AI_FeedbackDTO;
import com.minhnguyen.AI_habit_track.models.AISessionFeedback;
import com.minhnguyen.AI_habit_track.models.FocusSession;
import com.minhnguyen.AI_habit_track.repositories.AISessionFeedbackRepository;
import com.minhnguyen.AI_habit_track.repositories.FocusSessionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class AI_FeedbackServiceTest {

    @Mock
    private AISessionFeedbackRepository aiSessionFeedbackRepository;
    @Mock
    private FocusSessionRepository focusSessionRepository;
    @InjectMocks // This creates an instance of AI_FeedbackService and injects the mocks into it
    private AI_FeedbackService aiFeedbackService;

    @Test
    void testAIFeedbackService_saveFeedback_baseCase1(){
        //Given
        Long focusSessionId = 1L;
        Long AIFeedbackId = 100L;
        AI_FeedbackDTO feedbackDTO = new AI_FeedbackDTO(focusSessionId, 10,10,"Great !");
        FocusSession existedFocusSession = new FocusSession();
        existedFocusSession.setId(focusSessionId);

        when(focusSessionRepository.findById(focusSessionId)).thenReturn(Optional.of(existedFocusSession));
        when(aiSessionFeedbackRepository.save(any(AISessionFeedback.class))).thenAnswer(invocation -> {
            AISessionFeedback feedback = invocation.getArgument(0);
            feedback.setId(AIFeedbackId); // Simulate the DB generating an ID
            return feedback;
        });

        FocusSession updatedSession = aiFeedbackService.saveFeedback(focusSessionId, feedbackDTO);
        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getId()).isEqualTo(focusSessionId);
        assertThat(updatedSession.getAiSessionFeedback().getId()).isEqualTo(AIFeedbackId);
        assertThat(updatedSession.getAiSessionFeedback().getAiFocusScore()).isEqualTo(10);
        assertThat(updatedSession.getAiSessionFeedback().getAiNote()).isEqualTo("Great !");
    }

}

