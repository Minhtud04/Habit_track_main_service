package com.minhnguyen.AI_habit_track.services;

import com.minhnguyen.AI_habit_track.DTO.UserOAuthDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    /**
     * Retrieves user details from the current security context.
     * This method does not use @AuthenticationPrincipal, as that annotation is
     * intended for controller method arguments.
     *
     * @return UserOAuth DTO with user details, or null if not authenticated via OAuth2.
     */
    public UserOAuthDTO getUserDetails() {
        // 1. Get the current Authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Check if the user is authenticated and the principal is an OAuth2User
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();

            // 3. Extract attributes and create the DTO
            return new UserOAuthDTO(
                    principal.getAttribute("name"),
                    principal.getAttribute("email")
            );
        }

        // Return null or throw an exception if the user is not authenticated as expected
        return null;
    }
}