package com.platform.brickstore.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.platform.brickstore.api.domain.entity.Country;
import com.platform.brickstore.api.exception.ErrorCode;
import com.platform.brickstore.api.exception.NotFoundException;
import com.platform.brickstore.api.repository.CountryRepository;
import com.platform.brickstore.api.repository.CurrencyRepository;
import com.platform.brickstore.api.utility.UUIDv7;

import lombok.RequiredArgsConstructor;

/**
 * Service for Country business logic.
 */
@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;

    /**
     * Get country by ID.
     */
    public Country getById(String id) {
        return countryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Country not found with id: " + id, ErrorCode.COUNTRY_NOT_FOUND));
    }

    /**
     * Get country by ISO2 code.
     */
    public Country getByIso2(String iso2) {
        return countryRepository.findByIso2(iso2)
            .orElseThrow(() -> new NotFoundException("Country not found with ISO2: " + iso2, ErrorCode.COUNTRY_NOT_FOUND));
    }

    /**
     * Get country by PID.
     */
    public Country getByPid(String pid) {
        return countryRepository.findByPid(pid)
            .orElseThrow(() -> new NotFoundException("Country not found with pid: " + pid, ErrorCode.COUNTRY_NOT_FOUND));
    }

    /**
     * Get country by ISO3 code.
     */
    public Country getByIso3(String iso3) {
        return countryRepository.findByIso3(iso3)
            .orElseThrow(() -> new NotFoundException("Country not found with ISO3: " + iso3, ErrorCode.COUNTRY_NOT_FOUND));
    }

    /**
     * Get country by name.
     */
    public Country getByName(String name) {
        return countryRepository.findByName(name)
            .orElseThrow(() -> new IllegalArgumentException("Country not found with name: " + name));
    }

    /**
     * Get all countries.
     */
    // public List<Country> getAll() {
    //     return countryRepository.findAll();
    // }

    /**
     * Get all countries with pagination.
     *
     * @param page 0-based page number
     * @param size items per page
     */
    public List<Country> getAllPaginated(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid page or size parameters");
        }
        return countryRepository.findAll(page, size);
    }

    /**
     * Get total count of countries.
     */
    public long getTotalCount() {
        return countryRepository.count();
    }

    /**
     * Create a new country.
     */
    public Country create(Country country) {
        validateCountry(country);
        
        // Generate IDs if not provided
        if (country.getId() == null) {
            country.setId(UUIDv7.generate().toString());
        }
        if (country.getPid() == null) {
            country.setPid(UUIDv7.generate().toString());
        }

        LocalDateTime now = LocalDateTime.now();
        country.setCreatedAt(now);
        country.setUpdatedAt(now);

        return countryRepository.save(country);
    }

    /**
     * Get country together with its currencies by country id.
     */
    public CountryWithCurrencies getWithCurrenciesById(String id) {
        Country country = getById(id);
        var currencies = countryRepository.findCurrenciesByCountryId(id);
        return new CountryWithCurrencies(country, currencies);
    }

    public CountryWithCurrencies getWithCurrenciesByPid(String pid) {
        Country country = countryRepository.findByPid(pid)
            .orElseThrow(() -> new NotFoundException("Country not found with pid: " + pid, ErrorCode.COUNTRY_NOT_FOUND));
        var currencies = countryRepository.findCurrenciesByCountryPid(pid);
        return new CountryWithCurrencies(country, currencies);
    }

    public CountryWithCurrencies getWithCurrenciesByIso2(String iso2) {
        Country country = getByIso2(iso2);
        var currencies = countryRepository.findCurrenciesByCountryIso2(iso2);
        return new CountryWithCurrencies(country, currencies);
    }

    /**
     * Associate currency with country.
     */
    public boolean addCurrencyToCountry(String countryId, String currencyId) {
        if (!countryRepository.existsById(countryId)) {
            throw new NotFoundException("Country not found with id: " + countryId, ErrorCode.COUNTRY_NOT_FOUND);
        }
        if (!currencyRepository.existsById(currencyId)) {
            throw new NotFoundException("Currency not found with id: " + currencyId, ErrorCode.CURRENCY_NOT_FOUND);
        }
        return countryRepository.addCurrencyToCountry(countryId, currencyId);
    }

    /**
     * Remove association between country and currency.
     */
    public void removeCurrencyFromCountry(String countryId, String currencyId) {
        countryRepository.removeCurrencyFromCountry(countryId, currencyId);
    }

    /**
     * Update an existing country.
     */
    public Country update(String id, Country country) {
        Country existing = getById(id);
        validateCountry(country);

        existing.setPid(country.getPid());
        existing.setName(country.getName());
        existing.setIso2(country.getIso2());
        existing.setIso3(country.getIso3());
        existing.setCode(country.getCode());
        existing.setUpdatedAt(LocalDateTime.now());

        return countryRepository.update(existing);
    }

    /**
     * Delete a country by ID.
     */
    public void delete(String id) {
        if (!countryRepository.existsById(id)) {
            throw new NotFoundException("Country not found with id: " + id, ErrorCode.COUNTRY_NOT_FOUND);
        }
        countryRepository.deleteById(id);
    }

    /**
     * Check if country exists by ID.
     */
    public boolean exists(String id) {
        return countryRepository.existsById(id);
    }

    /**
     * Validate country data.
     */
    private void validateCountry(Country country) {
        if (country == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }
        if (country.getName() == null || country.getName().isBlank()) {
            throw new IllegalArgumentException("Country name cannot be blank");
        }
        if (country.getIso2() == null || country.getIso2().length() != 2) {
            throw new IllegalArgumentException("ISO2 code must be exactly 2 characters");
        }
        if (country.getIso3() == null || country.getIso3().length() != 3) {
            throw new IllegalArgumentException("ISO3 code must be exactly 3 characters");
        }
        if (country.getCode() == null || country.getCode() <= 0) {
            throw new IllegalArgumentException("Country code must be greater than 0");
        }
    }
}
