package com.platform.brickstore.api.dto;

import java.util.List;

/**
 * DTO for Currency with its countries (Java 21 record).
 */
public record CurrencyWithCountriesResponse(
    CurrencyResponse currency,
    List<CountryResponse> countries
) {}
