package com.platform.brickstore.api.dto;

/**
 * Simple DTO for Country used when nested within other responses (Java 21 record).
 */
public record CountrySimpleResponse(
    String pid,
    String name,
    String iso2,
    String iso3,
    Short code,
    String createdAt,
    String updatedAt
) {}
