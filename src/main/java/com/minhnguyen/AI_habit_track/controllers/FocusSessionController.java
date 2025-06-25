package com.minhnguyen.AI_habit_track.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FocusSessionController {

    @GetMapping("/session")
    public ResponseEntity<T> getSession() {

    }
}
