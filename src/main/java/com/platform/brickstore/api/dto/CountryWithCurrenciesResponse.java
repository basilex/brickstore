package com.platform.brickstore.api.dto;

import java.util.List;

/**
 * DTO for Country with its currencies (Java 21 record).
 */
public record CountryWithCurrenciesResponse(
    CountryResponse country,
    List<CurrencyResponse> currencies
) {}
