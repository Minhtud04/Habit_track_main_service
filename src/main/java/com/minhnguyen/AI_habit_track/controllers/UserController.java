package com.minhnguyen.AI_habit_track.controllers;
import jakarta.validation.Valid;
import com.minhnguyen.AI_habit_track.models.User;
import com.minhnguyen.AI_habit_track.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/oauth2/user/info")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            String name = principal.getAttribute("name");
            String email = principal.getAttribute("email");
            System.out.println("User Name: " + name);
            System.out.println("User Email: " + email);

            return principal.getAttributes();
        }
        return Map.of("error", "User not authenticated");
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.addNewUser(user);
        return ResponseEntity.ok(createdUser);
    }
}
