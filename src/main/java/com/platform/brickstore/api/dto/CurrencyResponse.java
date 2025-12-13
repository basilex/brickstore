package com.platform.brickstore.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Currency response DTO as Java 21 record (immutable, compact syntax).
 */
public record CurrencyResponse(
    String pid,
    String name,
    String iso3,
    Short code,
    String symbol,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt,
    List<CountrySimpleResponse> countries
) {}
