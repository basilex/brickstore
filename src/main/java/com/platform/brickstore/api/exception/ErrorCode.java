package com.platform.brickstore.api.exception;

/**
 * Common error codes for the API.
 */
public final class ErrorCode {

    // Country errors
    public static final String COUNTRY_NOT_FOUND = "COUNTRY_NOT_FOUND";
    public static final String COUNTRY_DUPLICATE = "COUNTRY_DUPLICATE";

    // Currency errors
    public static final String CURRENCY_NOT_FOUND = "CURRENCY_NOT_FOUND";
    public static final String CURRENCY_DUPLICATE = "CURRENCY_DUPLICATE";


    // RBAC Role errors
    public static final String ROLE_NOT_FOUND = "ROLE_NOT_FOUND";
    public static final String ROLE_DUPLICATE = "ROLE_DUPLICATE";

    // Validation errors
    public static final String INVALID_REQUEST = "INVALID_REQUEST";

    private ErrorCode() {
        // Utility class
    }
}
