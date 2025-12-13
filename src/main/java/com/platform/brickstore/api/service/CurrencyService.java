package com.platform.brickstore.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.platform.brickstore.api.domain.entity.Currency;
import com.platform.brickstore.api.exception.ErrorCode;
import com.platform.brickstore.api.exception.NotFoundException;
import com.platform.brickstore.api.repository.CountryRepository;
import com.platform.brickstore.api.repository.CurrencyRepository;
import com.platform.brickstore.api.utility.UUIDv7;

import lombok.RequiredArgsConstructor;

/**
 * Service for Currency business logic.
 */
@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CountryRepository countryRepository;

    /**
     * Get currency by ID.
     */
    public Currency getById(String id) {
        return currencyRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Currency not found with id: " + id, ErrorCode.CURRENCY_NOT_FOUND));
    }

    /**
     * Get currency by ISO3 code.
     */
    public Currency getByIso3(String iso3) {
        return currencyRepository.findByIso3(iso3)
            .orElseThrow(() -> new NotFoundException("Currency not found with ISO3: " + iso3, ErrorCode.CURRENCY_NOT_FOUND));
    }

    /**
     * Get currency by PID.
     */
    public Currency getByPid(String pid) {
        return currencyRepository.findByPid(pid)
            .orElseThrow(() -> new NotFoundException("Currency not found with pid: " + pid, ErrorCode.CURRENCY_NOT_FOUND));
    }

    /**
     * Get currency by name.
     */
    public Currency getByName(String name) {
        return currencyRepository.findByName(name)
            .orElseThrow(() -> new IllegalArgumentException("Currency not found with name: " + name));
    }

    /**
     * Get all currencies.
     */
    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    /**
     * Get all currencies with pagination.
     *
     * @param page 0-based page number
     * @param size items per page
     */
    public List<Currency> getAllPaginated(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid page or size parameters");
        }
        return currencyRepository.findAll(page, size);
    }

    /**
     * Get total count of currencies.
     */
    public long getTotalCount() {
        return currencyRepository.count();
    }

    /**
     * Create a new currency.
     */
    public Currency create(Currency currency) {
        validateCurrency(currency);
        
        // Generate IDs if not provided
        if (currency.getId() == null) {
            currency.setId(UUIDv7.generate().toString());
        }
        if (currency.getPid() == null) {
            currency.setPid(UUIDv7.generate().toString());
        }

        LocalDateTime now = LocalDateTime.now();
        currency.setCreatedAt(now);
        currency.setUpdatedAt(now);

        return currencyRepository.save(currency);
    }

    /**
     * Get currency together with its countries by currency id.
     */
    public CurrencyWithCountries getWithCountriesById(String id) {
        Currency currency = getById(id);
        var countries = currencyRepository.findCountriesByCurrencyId(id);
        return new CurrencyWithCountries(currency, countries);
    }

    public CurrencyWithCountries getWithCountriesByPid(String pid) {
        Currency currency = currencyRepository.findByPid(pid)
            .orElseThrow(() -> new NotFoundException("Currency not found with pid: " + pid, ErrorCode.CURRENCY_NOT_FOUND));
        var countries = currencyRepository.findCountriesByCurrencyPid(pid);
        return new CurrencyWithCountries(currency, countries);
    }

    public CurrencyWithCountries getWithCountriesByIso3(String iso3) {
        Currency currency = getByIso3(iso3);
        var countries = currencyRepository.findCountriesByCurrencyIso3(iso3);
        return new CurrencyWithCountries(currency, countries);
    }

    /**
     * Associate country with currency.
     */
    public boolean addCountryToCurrency(String countryId, String currencyId) {
        if (!currencyRepository.existsById(currencyId)) {
            throw new NotFoundException("Currency not found with id: " + currencyId, ErrorCode.CURRENCY_NOT_FOUND);
        }
        if (!countryRepository.existsById(countryId)) {
            throw new NotFoundException("Country not found with id: " + countryId, ErrorCode.COUNTRY_NOT_FOUND);
        }
        return currencyRepository.addCountryToCurrency(countryId, currencyId);
    }

    /**
     * Remove association between currency and country.
     */
    public void removeCountryFromCurrency(String countryId, String currencyId) {
        currencyRepository.removeCountryFromCurrency(countryId, currencyId);
    }

    /**
     * Update an existing currency.
     */
    public Currency update(String id, Currency currency) {
        Currency existing = getById(id);
        validateCurrency(currency);

        existing.setPid(currency.getPid());
        existing.setName(currency.getName());
        existing.setIso3(currency.getIso3());
        existing.setCode(currency.getCode());
        existing.setSymbol(currency.getSymbol());
        existing.setUpdatedAt(LocalDateTime.now());

        return currencyRepository.update(existing);
    }

    /**
     * Delete a currency by ID.
     */
    public void delete(String id) {
        if (!currencyRepository.existsById(id)) {
            throw new NotFoundException("Currency not found with id: " + id, ErrorCode.CURRENCY_NOT_FOUND);
        }
        currencyRepository.deleteById(id);
    }

    /**
     * Check if currency exists by ID.
     */
    public boolean exists(String id) {
        return currencyRepository.existsById(id);
    }

    /**
     * Validate currency data.
     */
    private void validateCurrency(Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        if (currency.getName() == null || currency.getName().isBlank()) {
            throw new IllegalArgumentException("Currency name cannot be blank");
        }
        if (currency.getIso3() == null || currency.getIso3().length() != 3) {
            throw new IllegalArgumentException("ISO3 code must be exactly 3 characters");
        }
        if (currency.getCode() == null || currency.getCode() <= 0) {
            throw new IllegalArgumentException("Currency code must be greater than 0");
        }
        if (currency.getSymbol() == null || currency.getSymbol().isBlank()) {
            throw new IllegalArgumentException("Currency symbol cannot be blank");
        }
    }
}
