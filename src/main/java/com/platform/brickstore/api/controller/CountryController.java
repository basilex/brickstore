package com.platform.brickstore.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.brickstore.api.dto.ActionResultResponse;
import com.platform.brickstore.api.dto.CountryRequest;
import com.platform.brickstore.api.dto.CountryResponse;
import com.platform.brickstore.api.mapper.CountryMapper;
import com.platform.brickstore.api.service.CountryService;
import com.platform.brickstore.api.service.CurrencyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for Country operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;
    private final CurrencyService currencyService;
    private final CountryMapper countryMapper;

    /**
     * Get all countries.
     * GET /api/v1/countries
     */
    @GetMapping
    public ResponseEntity<List<CountryResponse>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var list = countryService.getAllPaginated(page, size).stream()
            .map(countryMapper::toResponse)
            .toList();
        return ResponseEntity.ok(list);
    }

    /**
     * Get country by PID.
     * GET /api/v1/countries/{pid}
     */
    @GetMapping("/{pid}")
    public ResponseEntity<CountryResponse> getByPid(@PathVariable String pid) {
        var entity = countryService.getByPid(pid);
        return ResponseEntity.ok(countryMapper.toResponse(entity));
    }

    /**
     * Get country by ISO2 code.
     * GET /api/v1/countries/iso2/{iso2}
     */
    @GetMapping("/iso2/{iso2}")
    public ResponseEntity<CountryResponse> getByIso2(@PathVariable String iso2) {
        return ResponseEntity.ok(countryMapper.toResponse(countryService.getByIso2(iso2)));
    }

    /**
     * Get country by ISO3 code.
     * GET /api/v1/countries/iso3/{iso3}
     */
    @GetMapping("/iso3/{iso3}")
    public ResponseEntity<CountryResponse> getByIso3(@PathVariable String iso3) {
        return ResponseEntity.ok(countryMapper.toResponse(countryService.getByIso3(iso3)));
    }

    /**
     * Get country with currencies by country pid.
     */
    @GetMapping("/{pid}/currencies")
    public ResponseEntity<CountryResponse> getCurrenciesForCountry(@PathVariable String pid) {
        var pair = countryService.getWithCurrenciesByPid(pid);
        CountryResponse resp = countryMapper.toResponse(pair.country(), pair.currencies());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/pid/{pid}/currencies")
    public ResponseEntity<CountryResponse> getCurrenciesForCountryByPid(@PathVariable String pid) {
        var pair = countryService.getWithCurrenciesByPid(pid);
        CountryResponse resp = countryMapper.toResponse(pair.country(), pair.currencies());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/iso2/{iso2}/currencies")
    public ResponseEntity<CountryResponse> getCurrenciesForCountryByIso2(@PathVariable String iso2) {
        var pair = countryService.getWithCurrenciesByIso2(iso2);
        CountryResponse resp = countryMapper.toResponse(pair.country(), pair.currencies());
        return ResponseEntity.ok(resp);
    }

    /**
     * Associate a currency with a country (both by PID).
     */
    @PostMapping("/{pid}/currencies/{currencyPid}")
    public ResponseEntity<ActionResultResponse> addCurrencyToCountry(@PathVariable String pid, @PathVariable String currencyPid) {
        var country = countryService.getByPid(pid);
        var currency = currencyService.getByPid(currencyPid);
        boolean created = countryService.addCurrencyToCountry(country.getId(), currency.getId());
        var body = new ActionResultResponse(created);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        }
        return ResponseEntity.ok(body);
    }

    /**
     * Remove association between a currency and a country (by PID).
     */
    @DeleteMapping("/{pid}/currencies/{currencyPid}")
    public ResponseEntity<Void> removeCurrencyFromCountry(@PathVariable String pid, @PathVariable String currencyPid) {
        var country = countryService.getByPid(pid);
        var currency = currencyService.getByPid(currencyPid);
        countryService.removeCurrencyFromCountry(country.getId(), currency.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Get country by name.
     * GET /api/v1/countries/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<CountryResponse> getByName(@RequestParam String name) {
        return ResponseEntity.ok(countryMapper.toResponse(countryService.getByName(name)));
    }

    /**
     * Get total count of countries.
     * GET /api/v1/countries/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(countryService.getTotalCount());
    }

    /**
     * Create a new country.
     * POST /api/v1/countries
     */
    @PostMapping
    public ResponseEntity<CountryResponse> create(@Valid @RequestBody CountryRequest request) {
        var toCreate = countryMapper.toEntity(request);
        var created = countryService.create(toCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(countryMapper.toResponse(created));
    }

    /**
     * Update a country.
     * PUT /api/v1/countries/{pid}
     */
    @PutMapping("/{pid}")
    public ResponseEntity<CountryResponse> update(
            @PathVariable String pid,
            @Valid @RequestBody CountryRequest request
    ) {
        var existing = countryService.getByPid(pid);
        var toUpdate = countryMapper.updateEntity(request, existing);
        var updated = countryService.update(existing.getId(), toUpdate);
        return ResponseEntity.ok(countryMapper.toResponse(updated));
    }

    /**
     * Delete a country.
     * DELETE /api/v1/countries/{pid}
     */
    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> delete(@PathVariable String pid) {
        var existing = countryService.getByPid(pid);
        countryService.delete(existing.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if country exists.
     * HEAD /api/v1/countries/{pid}
     */
    @RequestMapping(method = RequestMethod.HEAD, value = "/{pid}")
    public ResponseEntity<Void> exists(@PathVariable String pid) {
        try {
            countryService.getByPid(pid);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
