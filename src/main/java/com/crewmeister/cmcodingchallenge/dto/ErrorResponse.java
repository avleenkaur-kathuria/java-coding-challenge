package com.crewmeister.cmcodingchallenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Error response DTO for OpenAPI documentation.
 * Represents the standardized error response format used by the application.
 */
@Schema(description = "Standardized error response format")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error code for programmatic handling", example = "INVALID_INPUT")
    private String error;

    @Schema(description = "Human-readable error message", example = "Amount must be a positive number. Received: -100")
    private String message;

    @Schema(description = "Request path that triggered the error", example = "/api/convert")
    private String path;

    @Schema(description = "Timestamp when the error occurred", example = "2024-05-05T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Unique trace ID for debugging", example = "a1b2c3d4-e5f6-47g8-h9i0-j1k2l3m4n5o6")
    private String traceId;

    @Schema(description = "Field-level validation errors (optional)")
    private List<FieldError> fieldErrors;

    /**
     * Represents a field-level validation error
     */
    @Schema(description = "Field-level validation error details")
    public static class FieldError {
        @Schema(description = "Field name that failed validation", example = "amount")
        private String field;

        @Schema(description = "Validation error message", example = "must be greater than 0")
        private String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    // Getters and setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}

