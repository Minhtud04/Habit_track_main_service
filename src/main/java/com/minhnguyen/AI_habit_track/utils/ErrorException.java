package com.minhnguyen.AI_habit_track.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ErrorException {
    /**
     * Thrown when a resource is looked for but not found.
     * Maps to HTTP 404 Not Found.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFound extends RuntimeException {
        public ResourceNotFound(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a client tries to create a resource that already exists.
     * Maps to HTTP 409 Conflict.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    public static class ResourceAlreadyExists extends RuntimeException {
        public ResourceAlreadyExists(String message) {
            super(message);
        }
    }

}

