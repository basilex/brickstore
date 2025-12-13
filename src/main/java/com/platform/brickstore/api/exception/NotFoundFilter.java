package com.platform.brickstore.api.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class NotFoundFilter extends OncePerRequestFilter {
    private static final String API_PREFIX = "/api/";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(request, response);
        if (response.getStatus() == 404 && request.getRequestURI().startsWith(API_PREFIX) && !response.isCommitted()) {
            response.resetBuffer();
            response.setStatus(404);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            var body = Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "status", 404,
                    "error", "Not Found",
                    "message", "No handler found for " + request.getMethod() + " " + request.getRequestURI(),
                    "details", null
            );
            OBJECT_MAPPER.writeValue(response.getWriter(), body);
        }
    }
}
