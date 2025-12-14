package com.platform.brickstore.api.exception;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler for REST API using RFC7807 ProblemDetail.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final @Nonnull URI PROBLEM_TYPE_BLANK = URI.create("about:blank");

    private ProblemDetail buildProblem(@Nonnull HttpStatusCode status, String title, String detail, Map<String, String> extra) {
        ProblemDetail pd = ProblemDetail.forStatus(Objects.requireNonNull(status));
        pd.setTitle(title);
        pd.setDetail(detail);
        pd.setType(Objects.requireNonNull(PROBLEM_TYPE_BLANK));
        pd.setProperty("timestamp", Instant.now().toString());
        // keep legacy field names for backwards compatibility with existing tests/clients
        pd.setProperty("error", title);
        if (extra != null && !extra.isEmpty()) {
            pd.setProperty("details", extra);
        }
        return pd;
    }

    private void attachInstanceAndRequestId(ProblemDetail pd, WebRequest webRequest) {
        if (webRequest instanceof ServletWebRequest sw) {
            HttpServletRequest req = sw.getRequest();
            Object attr = req.getAttribute("requestId");
            String requestId = null;
            if (attr instanceof String s) {
                requestId = s;
            } else {
                String h = req.getHeader("X-Request-Id");
                if (h != null && !h.isBlank()) requestId = h;
            }
            if (requestId != null && !requestId.isBlank()) {
                // RFC7807 'instance' is a URI identifying the specific occurrence
                try {
                    pd.setInstance(URI.create("urn:uuid:" + requestId));
                } catch (Exception e) {
                    // ignore malformed id for instance
                }
                pd.setProperty("requestId", requestId);
            }
        }
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoResourceFound(NoResourceFoundException ex, WebRequest request) {
        ProblemDetail pd = buildProblem(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage(), null);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        ProblemDetail pd = buildProblem(HttpStatus.BAD_REQUEST, "Validation Error", "Invalid input parameters", errors);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        log.debug("Malformed JSON request", ex);
        ProblemDetail pd = buildProblem(HttpStatus.BAD_REQUEST, "Bad Request", "Malformed JSON request", null);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv ->
            errors.put(cv.getPropertyPath().toString(), cv.getMessage())
        );
        ProblemDetail pd = buildProblem(HttpStatus.BAD_REQUEST, "Validation Error", "Constraint violations", errors);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put(ex.getName(), "Invalid value: " + ex.getValue());
        ProblemDetail pd = buildProblem(HttpStatus.BAD_REQUEST, "Bad Request", "Method argument type mismatch", details);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingParams(MissingServletRequestParameterException ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put(ex.getParameterName(), "Parameter is missing");
        ProblemDetail pd = buildProblem(HttpStatus.BAD_REQUEST, "Bad Request", "Missing request parameter", details);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        ProblemDetail pd = buildProblem(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed", ex.getMessage(), null);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        ProblemDetail pd = buildProblem(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type", ex.getMessage(), null);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoHandlerFound(NoHandlerFoundException ex, WebRequest request) {
        ProblemDetail pd = buildProblem(HttpStatus.NOT_FOUND, "Not Found", "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL(), null);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrity(DataIntegrityViolationException ex, WebRequest request) {
        log.debug("Data integrity violation", ex);
        ProblemDetail pd = buildProblem(HttpStatus.CONFLICT, "Conflict", "Data integrity violation", null);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateKey(DuplicateKeyException ex, WebRequest request) {
        log.debug("Duplicate key", ex);
        ProblemDetail pd = buildProblem(HttpStatus.CONFLICT, "Conflict", "Duplicate key violates unique constraint", null);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ProblemDetail> handleEmptyResult(EmptyResultDataAccessException ex, WebRequest request) {
        ProblemDetail pd = buildProblem(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), null);
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(NotFoundException ex, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        details.put("errorCode", ex.getErrorCode());
        ProblemDetail pd = buildProblem(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), details);
        pd.setProperty("errorCode", ex.getErrorCode());
        attachInstanceAndRequestId(pd, request);
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle IllegalArgumentException.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        ProblemDetail pd = buildProblem(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), null);
        attachInstanceAndRequestId(pd, request);

        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle general exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(
            Exception ex,
            WebRequest request
    ) {
        // Detect common security exceptions by class name at runtime (no compile-time dependency on Spring Security)
        String secType = findSecurityExceptionType(ex);
        if ("ACCESS_DENIED".equals(secType)) {
            ProblemDetail pd = buildProblem(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage() != null ? ex.getMessage() : "Access is denied", null);
            attachInstanceAndRequestId(pd, request);
            return new ResponseEntity<>(pd, HttpStatus.FORBIDDEN);
        }
        if ("AUTHENTICATION".equals(secType)) {
            ProblemDetail pd = buildProblem(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage() != null ? ex.getMessage() : "Authentication required", null);
            attachInstanceAndRequestId(pd, request);
            return new ResponseEntity<>(pd, HttpStatus.UNAUTHORIZED);
        }

        // Fallback: internal server error
        log.warn("{}: {}", ex.getClass().getName(), ex.getMessage());
        ProblemDetail pd = buildProblem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred", null);
        attachInstanceAndRequestId(pd, request);

        return new ResponseEntity<>(pd, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Inspect exception and causes for known Spring Security exception class names.
     * Returns "ACCESS_DENIED" or "AUTHENTICATION" when detected, otherwise null.
     */
    private String findSecurityExceptionType(Throwable t) {
        Throwable cur = t;
        while (cur != null) {
            String name = cur.getClass().getName();
            if ("org.springframework.security.access.AccessDeniedException".equals(name)) {
                return "ACCESS_DENIED";
            }
            if (name.equals("org.springframework.security.core.AuthenticationException")
                || name.startsWith("org.springframework.security.authentication.")) {
                return "AUTHENTICATION";
            }
            cur = cur.getCause();
        }
        return null;
    }
}

