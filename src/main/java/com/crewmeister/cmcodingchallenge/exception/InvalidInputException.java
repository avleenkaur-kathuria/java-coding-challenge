package com.crewmeister.cmcodingchallenge.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when input validation fails.
 */
public class InvalidInputException extends ApplicationException {
    public InvalidInputException(String message) {
        super(
            message,
            HttpStatus.BAD_REQUEST.value(),
            "INVALID_INPUT"
        );
    }

    public InvalidInputException(String message, Throwable cause) {
        super(
            message,
            cause,
            HttpStatus.BAD_REQUEST.value(),
            "INVALID_INPUT"
        );
    }
}

