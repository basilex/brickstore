package com.platform.brickstore.api.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Small ErrorAttributes implementation that returns ProblemDetail-like attributes
 * for the default `/error` endpoint. Keeps keys compatible with our handlers/tests.
 */
@Component
public class ProblemErrorAttributes implements ErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        ServletWebRequest sw = (ServletWebRequest) webRequest;
        HttpServletRequest req = sw.getRequest();

        Map<String, Object> attrs = new LinkedHashMap<>();

        Object statusObj = webRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE, WebRequest.SCOPE_REQUEST);
        int status = statusObj instanceof Integer ? (Integer) statusObj : 500;

        attrs.put("timestamp", Instant.now().toString());
        attrs.put("status", status);
        // RFC7807 'type' - default to about:blank; may be overridden below for domain errors
        String type = "about:blank";
        attrs.put("type", type);
        attrs.put("error", req.getAttribute(RequestDispatcher.ERROR_MESSAGE) != null ? String.valueOf(req.getAttribute(RequestDispatcher.ERROR_MESSAGE)) : org.springframework.http.HttpStatus.resolve(status) != null ? org.springframework.http.HttpStatus.resolve(status).getReasonPhrase() : "Error");

        Throwable ex = getError(webRequest);
        String message = ex != null && ex.getMessage() != null ? ex.getMessage() : String.valueOf(webRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE, WebRequest.SCOPE_REQUEST));
        attrs.put("message", message);

        Object path = webRequest.getAttribute(RequestDispatcher.ERROR_REQUEST_URI, WebRequest.SCOPE_REQUEST);
        attrs.put("path", path != null ? String.valueOf(path) : req.getRequestURI());

        // preserve requestId if present and set instance URN
        Object requestId = req.getAttribute("requestId");
        if (requestId == null) requestId = req.getHeader("X-Request-Id");
        if (requestId != null) {
            String rid = String.valueOf(requestId);
            attrs.put("requestId", rid);
            // RFC7807 'instance' - use urn:uuid:<requestId> when present
            attrs.put("instance", "urn:uuid:" + rid);
        }

        if (ex instanceof NotFoundException nf) {
            Map<String, String> details = Map.of("errorCode", nf.getErrorCode());
            attrs.put("details", details);
            attrs.put("errorCode", nf.getErrorCode());
            // map known errorCode to a domain-specific problem type URI, e.g. COUNTRY_NOT_FOUND -> /problems/country-not-found
            try {
                String ec = nf.getErrorCode();
                if (ec != null && !ec.isBlank()) {
                    String candidate = "/problems/" + ec.toLowerCase().replace('_', '-');
                    attrs.put("type", candidate);
                }
            } catch (Exception ignore) {
            }
        }

        return attrs;
    }

    @Override
    public Throwable getError(WebRequest webRequest) {
        Object ex = webRequest.getAttribute(RequestDispatcher.ERROR_EXCEPTION, WebRequest.SCOPE_REQUEST);
        if (ex instanceof Throwable) return (Throwable) ex;
        return null;
    }
}
