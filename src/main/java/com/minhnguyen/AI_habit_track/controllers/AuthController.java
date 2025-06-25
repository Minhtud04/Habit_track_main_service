package com.minhnguyen.AI_habit_track.controllers;

import com.minhnguyen.AI_habit_track.DTO.UserOAuth;
import com.minhnguyen.AI_habit_track.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {
    AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @GetMapping("/oauth2/info")
    public ResponseEntity<UserOAuth> userDetails(@AuthenticationPrincipal OAuth2User principal) {
        UserOAuth userOAuth = authService.userDetails(principal);
        return ResponseEntity.ok(userOAuth);
    }
}
