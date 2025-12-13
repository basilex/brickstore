package com.platform.brickstore.api.service;

import java.util.List;

import com.platform.brickstore.api.domain.entity.Country;
import com.platform.brickstore.api.domain.entity.Currency;

/**
 * Simple container for Country and its currencies.
 */
public record CountryWithCurrencies(Country country, List<Currency> currencies) {
}
