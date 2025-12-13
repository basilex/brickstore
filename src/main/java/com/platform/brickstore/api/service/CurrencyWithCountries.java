package com.platform.brickstore.api.service;

import java.util.List;

import com.platform.brickstore.api.domain.entity.Country;
import com.platform.brickstore.api.domain.entity.Currency;

/**
 * Simple container for Currency and its countries.
 */
public record CurrencyWithCountries(Currency currency, List<Country> countries) {
}
