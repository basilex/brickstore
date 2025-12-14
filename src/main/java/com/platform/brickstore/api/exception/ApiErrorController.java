package com.platform.brickstore.api.exception;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Handles the generic /error endpoint and returns RFC7807 ProblemDetail responses.
 */
@RestController
@RequiredArgsConstructor
public class ApiErrorController {

    private final ErrorAttributes errorAttributes;

    @RequestMapping("${server.error.path:${error.path:/error}}")
    public ResponseEntity<ProblemDetail> handleError(HttpServletRequest request) {
        ServletWebRequest sw = new ServletWebRequest(request);
        Map<String, Object> attrs = errorAttributes.getErrorAttributes(sw, ErrorAttributeOptions.defaults());
        int status = 500;
        Object st = attrs.get("status");
        if (st instanceof Integer) status = (Integer) st;
        String error = String.valueOf(attrs.getOrDefault("error", "Error"));
        String message = String.valueOf(attrs.getOrDefault("message", "Unexpected error"));

        HttpStatus httpStatus = HttpStatus.resolve(status);
        if (httpStatus == null) httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail pd = ProblemDetail.forStatus(httpStatus);
        pd.setTitle(error);
        pd.setDetail(message);
        pd.setType(URI.create("about:blank"));
        pd.setProperty("timestamp", Instant.now().toString());

        // preserve requestId if present as attribute/header
        Object requestId = request.getAttribute("requestId");
        if (requestId == null) requestId = request.getHeader("X-Request-Id");
        if (requestId != null) {
            String rid = String.valueOf(requestId);
            pd.setProperty("requestId", rid);
            try { pd.setInstance(URI.create("urn:uuid:" + rid)); } catch (Exception ignored) {}
        }

        return ResponseEntity.status(httpStatus).body(pd);
    }
}
