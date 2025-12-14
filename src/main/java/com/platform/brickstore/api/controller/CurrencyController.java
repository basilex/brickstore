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
import com.platform.brickstore.api.dto.CurrencyRequest;
import com.platform.brickstore.api.dto.CurrencyResponse;
import com.platform.brickstore.api.mapper.CurrencyMapper;
import com.platform.brickstore.api.service.CountryService;
import com.platform.brickstore.api.service.CurrencyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for Currency operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CountryService countryService;
    private final CurrencyMapper currencyMapper;

    /**
     * Get all currencies.
     * GET /api/v1/currencies
     */
    @GetMapping
    public ResponseEntity<List<CurrencyResponse>> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var list = currencyService.getAllPaginated(page, size).stream()
            .map(currencyMapper::toResponse)
            .toList();
        return ResponseEntity.ok(list);
    }

    /**
     * Get currency by PID.
     * GET /api/v1/currencies/{pid}
     */
    @GetMapping("/{pid}")
    public ResponseEntity<CurrencyResponse> getByPid(@PathVariable String pid) {
        var entity = currencyService.getByPid(pid);
        return ResponseEntity.ok(currencyMapper.toResponse(entity));
    }

    /**
     * Get currency by ISO3 code.
     * GET /api/v1/currencies/iso3/{iso3}
     */
    @GetMapping("/iso3/{iso3}")
    public ResponseEntity<CurrencyResponse> getByIso3(@PathVariable String iso3) {
        return ResponseEntity.ok(currencyMapper.toResponse(currencyService.getByIso3(iso3)));
    }

    /**
     * Get currency with countries by currency pid.
     */
    @GetMapping("/{pid}/countries")
    public ResponseEntity<CurrencyResponse> getCountriesForCurrency(@PathVariable String pid) {
        var pair = currencyService.getWithCountriesByPid(pid);
        var resp = currencyMapper.toResponse(pair.currency(), pair.countries());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/pid/{pid}/countries")
    public ResponseEntity<CurrencyResponse> getCountriesForCurrencyByPid(@PathVariable String pid) {
        var pair = currencyService.getWithCountriesByPid(pid);
        var resp = currencyMapper.toResponse(pair.currency(), pair.countries());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/iso3/{iso3}/countries")
    public ResponseEntity<CurrencyResponse> getCountriesForCurrencyByIso3(@PathVariable String iso3) {
        var pair = currencyService.getWithCountriesByIso3(iso3);
        var resp = currencyMapper.toResponse(pair.currency(), pair.countries());
        return ResponseEntity.ok(resp);
    }

    /**
     * Associate a country with a currency (both by PID).
     */
    @PostMapping("/{pid}/countries/{countryPid}")
    public ResponseEntity<ActionResultResponse> addCountryToCurrency(@PathVariable String pid, @PathVariable String countryPid) {
        var currency = currencyService.getByPid(pid);
        var country = countryService.getByPid(countryPid);
        boolean created = currencyService.addCountryToCurrency(country.getId(), currency.getId());
        var body = new ActionResultResponse(created);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        }
        return ResponseEntity.ok(body);
    }

    /**
     * Remove association between a country and a currency (by PID).
     */
    @DeleteMapping("/{pid}/countries/{countryPid}")
    public ResponseEntity<Void> removeCountryFromCurrency(@PathVariable String pid, @PathVariable String countryPid) {
        var currency = currencyService.getByPid(pid);
        var country = countryService.getByPid(countryPid);
        currencyService.removeCountryFromCurrency(country.getId(), currency.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Get currency by name.
     * GET /api/v1/currencies/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<CurrencyResponse> getByName(@RequestParam String name) {
        return ResponseEntity.ok(currencyMapper.toResponse(currencyService.getByName(name)));
    }

    /**
     * Get total count of currencies.
     * GET /api/v1/currencies/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(currencyService.getTotalCount());
    }

    /**
     * Create a new currency.
     * POST /api/v1/currencies
     */
    @PostMapping
    public ResponseEntity<CurrencyResponse> create(@Valid @RequestBody CurrencyRequest request) {
        var toCreate = currencyMapper.toEntity(request);
        var created = currencyService.create(toCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(currencyMapper.toResponse(created));
    }

    /**
     * Update a currency.
     * PUT /api/v1/currencies/{pid}
     */
    @PutMapping("/{pid}")
    public ResponseEntity<CurrencyResponse> update(
            @PathVariable String pid,
            @Valid @RequestBody CurrencyRequest request
    ) {
        var existing = currencyService.getByPid(pid);
        var toUpdate = currencyMapper.updateEntity(request, existing);
        var updated = currencyService.update(existing.getId(), toUpdate);
        return ResponseEntity.ok(currencyMapper.toResponse(updated));
    }

    /**
     * Delete a currency.
     * DELETE /api/v1/currencies/{pid}
     */
    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> delete(@PathVariable String pid) {
        var existing = currencyService.getByPid(pid);
        currencyService.delete(existing.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if currency exists.
     * HEAD /api/v1/currencies/{pid}
     */
    @RequestMapping(method = RequestMethod.HEAD, value = "/{pid}")
    public ResponseEntity<Void> exists(@PathVariable String pid) {
        try {
            currencyService.getByPid(pid);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
