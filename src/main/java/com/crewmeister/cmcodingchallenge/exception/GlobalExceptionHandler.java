package com.crewmeister.cmcodingchallenge.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Global exception handler for all REST API endpoints.
 * Provides consistent error responses across the application.
 * All exceptions are logged with a unique trace ID for debugging.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles ApplicationException and its subclasses.
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> handleApplicationException(
            ApplicationException ex,
            WebRequest request) {
        
        String traceId = UUID.randomUUID().toString();
        logger.warn("Application exception occurred [TraceId: {}]: {} [ErrorCode: {}]",
                traceId, ex.getMessage(), ex.getErrorCode());
        
        ApiErrorResponse response = new ApiErrorResponse(
                ex.getHttpStatus(),
                ex.getErrorCode(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        response.setTraceId(traceId);
        
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getHttpStatus()));
    }

    /**
     * Handles validation errors from @Valid annotation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        
        String traceId = UUID.randomUUID().toString();
        List<ApiErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.add(new ApiErrorResponse.FieldError(
                        error.getField(),
                        error.getDefaultMessage()))
        );
        
        logger.warn("Validation error occurred [TraceId: {}]: {} field(s) failed validation",
                traceId, fieldErrors.size());
        
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_FAILED",
                "Input validation failed",
                request.getDescription(false).replace("uri=", "")
        );
        response.setTraceId(traceId);
        response.setFieldErrors(fieldErrors);
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles type mismatch errors (e.g., passing string where number expected).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        
        String traceId = UUID.randomUUID().toString();
        String message = String.format(
                "Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(),
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        );
        
        logger.warn("Type mismatch error [TraceId: {}]: {}", traceId, message);
        
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_PARAMETER_TYPE",
                message,
                request.getDescription(false).replace("uri=", "")
        );
        response.setTraceId(traceId);
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles 404 errors when endpoint is not found.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoHandlerFound(
            NoHandlerFoundException ex,
            WebRequest request) {
        
        String traceId = UUID.randomUUID().toString();
        String message = String.format("Endpoint '%s' not found", ex.getRequestURL());
        
        logger.warn("Endpoint not found [TraceId: {}]: {} {}", traceId, ex.getHttpMethod(), ex.getRequestURL());
        
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "ENDPOINT_NOT_FOUND",
                message,
                request.getDescription(false).replace("uri=", "")
        );
        response.setTraceId(traceId);
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles all other unexpected exceptions.
     * Should be the last resort - specific exceptions should be handled above.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(
            Exception ex,
            WebRequest request) {
        
        String traceId = UUID.randomUUID().toString();
        logger.error("Unexpected exception occurred [TraceId: {}]", traceId, ex);
        
        // Don't expose internal implementation details in production
        String message = "An unexpected error occurred. Please contact support with trace ID: " + traceId;
        
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                message,
                request.getDescription(false).replace("uri=", "")
        );
        response.setTraceId(traceId);
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

