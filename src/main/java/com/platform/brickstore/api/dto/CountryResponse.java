package com.platform.brickstore.api.dto;

import java.util.List;

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
    String createdAt,
    String updatedAt
) {}

