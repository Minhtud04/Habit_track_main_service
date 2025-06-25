package com.minhnguyen.AI_habit_track.services;

import com.minhnguyen.AI_habit_track.DTO.UserOAuth;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    public UserOAuth userDetails(@AuthenticationPrincipal OAuth2User principal) {
        // @AuthenticationPrincipal injects the currently logged-in user.
        // For OAuth2, this is an OAuth2User object which contains the user's attributes.
        if (principal != null) {
            return new UserOAuth(
                    principal.getAttribute("name"),
                    principal.getAttribute("email")
            );
        }
        return null;
    }
}
