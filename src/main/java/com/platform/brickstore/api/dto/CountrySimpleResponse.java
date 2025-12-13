package com.platform.brickstore.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Simple DTO for Country used when nested within other responses (Java 21 record).
 */
public record CountrySimpleResponse(
    String pid,
    String name,
    String iso2,
    String iso3,
    Short code,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt
) {}
