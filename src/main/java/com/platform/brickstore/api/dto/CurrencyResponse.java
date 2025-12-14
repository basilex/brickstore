package com.platform.brickstore.api.dto;

import java.util.List;

/**
 * Currency response DTO as Java 21 record (immutable, compact syntax).
 */
public record CurrencyResponse(
    String pid,
    String name,
    String iso3,
    Short code,
    String symbol,
    String createdAt,
    String updatedAt,
    List<CountrySimpleResponse> countries
) {}
