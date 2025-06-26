package com.minhnguyen.AI_habit_track.utils.errorHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.stream.Collectors;
import java.util.List;


@Order(Ordered.HIGHEST_PRECEDENCE) // Give this handler priority
@RestControllerAdvice
public class GlobalExceptionControllerHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionControllerHandler.class);

    /**
     * Handles ResourceNotFound exceptions.
     * Triggered when a requested resource (e.g., a specific user) does not exist.
     * Responds with HTTP 404 Not Found.
     */
    @ExceptionHandler(ErrorException.ResourceNotFound.class)
    protected ResponseEntity<Object> handleResourceNotFound(ErrorException.ResourceNotFound ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles ResourceAlreadyExists exceptions.
     * Triggered when attempting to create a resource that violates a unique constraint (e.g., duplicate email).
     * Responds with HTTP 409 Conflict.
     */
    @ExceptionHandler(ErrorException.ResourceAlreadyExists.class)
    protected ResponseEntity<Object> handleResourceAlreadyExists(ErrorException.ResourceAlreadyExists ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage(), request);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Overrides the handler for @Valid validation errors.
     * Provides detailed, field-specific error messages.
     * Responds with HTTP 400 Bad Request.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation Error", request);

        List<ApiSubError> subErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ApiValidationError(
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        apiError.setSubErrors(subErrors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * NEW: Handles malformed JSON and data type mismatch errors.
     * Triggered when the request body cannot be deserialized into the DTO.
     * Responds with HTTP 400 Bad Request.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        logger.warn("Malformed JSON request: {}", ex.getMessage());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Malformed JSON request", request);
        apiError.setDebugMessage("The request body is not well-formed or contains invalid data types.");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * NEW: A critical safety-net handler for all other unexpected exceptions.
     * Prevents stack traces from being exposed to the client.
     * Responds with HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("An unexpected error occurred", ex); // Log the full stack trace for debugging
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected internal error occurred", request);
        apiError.setDebugMessage(ex.getLocalizedMessage()); // Provide the exception message for debugging
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}