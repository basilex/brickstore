package com.platform.brickstore.api.exception;

/**
 * Thrown when a requested resource is not found.
 */
public class NotFoundException extends RuntimeException {

    private final String errorCode;

    public NotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
