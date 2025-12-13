package com.platform.brickstore.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Country response DTO as Java 21 record (immutable, compact syntax).
 */
public record CountryResponse(
    String pid,
    String name,
    String iso2,
    String iso3,
    Short code,
    List<CurrencyResponse> currencies,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt
) {}

