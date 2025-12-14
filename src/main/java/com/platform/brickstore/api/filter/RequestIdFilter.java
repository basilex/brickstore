package com.platform.brickstore.api.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter that ensures each request has a request id (`X-Request-Id`).
 * Also sets the id in MDC for logging correlation.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter extends OncePerRequestFilter {
    public static final String HEADER = "X-Request-Id";
    public static final String ATTR = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String reqId = request.getHeader(HEADER);
        if (reqId == null || reqId.isBlank()) {
            reqId = UUID.randomUUID().toString();
        }

        // put into request attribute and MDC
        request.setAttribute(ATTR, reqId);
        MDC.put(HEADER, reqId);

        // also add to response header for clients
        response.setHeader(HEADER, reqId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(HEADER);
        }
    }
}
