package com.crewmeister.cmcodingchallenge.exception;

/**
 * Base exception class for application-specific errors.
 * All business logic exceptions should extend this class.
 */
public class ApplicationException extends RuntimeException {
    private final int httpStatus;
    private final String errorCode;

    public ApplicationException(String message, int httpStatus, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public ApplicationException(String message, Throwable cause, int httpStatus, String errorCode) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

