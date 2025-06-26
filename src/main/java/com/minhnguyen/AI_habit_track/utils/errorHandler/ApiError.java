package com.minhnguyen.AI_habit_track.utils.errorHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Don't include null fields in the JSON
public class ApiError {  // Renamed to ApiError for clarity
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private String debugMessage; // Optional: for more detailed internal messages
    private List<ApiSubError> subErrors; // Optional: for validation errors

    public ApiError(int status, String message, WebRequest request) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = request.getDescription(false).replace("uri=", "");
    }

    // You can add more constructors as needed
}

// A simple interface for sub-errors might not be needed, but can be useful
interface ApiSubError {}

// A concrete class for validation errors
@Getter
@Setter
class ApiValidationError implements ApiSubError {
    private String field;
    private Object rejectedValue;
    private String message;

    public ApiValidationError(String field, Object rejectedValue, String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}