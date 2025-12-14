package com.platform.brickstore.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.platform.brickstore.api.domain.entity.Country;
import com.platform.brickstore.api.domain.entity.Currency;

import lombok.AllArgsConstructor;

/**
 * Repository for Currency entity using JdbcClient.
 */
@Repository
@AllArgsConstructor
public class CurrencyRepository {

    private static final String SQL_FIND_BY_ID = """
        SELECT id, pid, name, iso3, code, symbol, created_at, updated_at
          FROM currency WHERE id = ?
    """;

    private static final String SQL_FIND_BY_ISO3 = """
        SELECT id, pid, name, iso3, code, symbol, created_at, updated_at
          FROM currency WHERE iso3 = ?
    """;

    private static final String SQL_FIND_BY_PID = """
        SELECT id, pid, name, iso3, code, symbol, created_at, updated_at
          FROM currency WHERE pid = ?
    """;

    private static final String SQL_FIND_BY_NAME = """
        SELECT id, pid, name, iso3, code, symbol, created_at, updated_at
          FROM currency WHERE name = ?
    """;

    private static final String SQL_FIND_ALL = """
        SELECT id, pid, name, iso3, code, symbol, created_at, updated_at
          FROM currency ORDER BY name
    """;

    private static final String SQL_FIND_ALL_PAGINATED = """
        SELECT id, pid, name, iso3, code, symbol, created_at, updated_at
          FROM currency ORDER BY name LIMIT ? OFFSET ?
    """;

    private static final String SQL_COUNT = "SELECT COUNT(*) FROM currency";

    private static final String SQL_INSERT = """
        INSERT INTO currency (id, pid, name, iso3, code, symbol, created_at, updated_at)
          VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String SQL_UPDATE = """
        UPDATE currency SET pid = ?, name = ?, iso3 = ?, code = ?, symbol = ?, updated_at = ?
          WHERE id = ?
    """;

    private static final String SQL_DELETE = "DELETE FROM currency WHERE id = ?";

    private static final String SQL_EXISTS = "SELECT 1 FROM currency WHERE id = ? LIMIT 1";

    private static final String SQL_FIND_COUNTRIES_BY_CURRENCY_ID = """
        SELECT c.id, c.pid, c.name, c.iso2, c.iso3, c.code, c.created_at, c.updated_at
          FROM country c
          JOIN country_currency cc ON c.id = cc.country_id
          WHERE cc.currency_id = ? ORDER BY c.name
    """;

    private static final String SQL_FIND_COUNTRIES_BY_CURRENCY_PID = """
        SELECT c.id, c.pid, c.name, c.iso2, c.iso3, c.code, c.created_at, c.updated_at
          FROM country c
          JOIN country_currency cc ON c.id = cc.country_id
          JOIN currency cur ON cur.id = cc.currency_id
          WHERE cur.pid = ? ORDER BY c.name
    """;

    private static final String SQL_FIND_COUNTRIES_BY_CURRENCY_ISO3 = """
        SELECT c.id, c.pid, c.name, c.iso2, c.iso3, c.code, c.created_at, c.updated_at
          FROM country c
          JOIN country_currency cc ON c.id = cc.country_id
          JOIN currency cur ON cur.id = cc.currency_id
          WHERE cur.iso3 = ? ORDER BY c.name
    """;

    private static final String SQL_ADD_COUNTRY = """
        INSERT INTO country_currency (country_id, currency_id)
          VALUES (?, ?)
          ON CONFLICT (country_id, currency_id) DO NOTHING RETURNING id
    """;

    private static final String SQL_REMOVE_COUNTRY = """
        DELETE FROM country_currency WHERE country_id = ? AND currency_id = ?
    """;

    private final JdbcClient jdbcClient;

    /**
     * Find a currency by its ID.
     */
    public Optional<Currency> findById(String id) {
        return jdbcClient.sql(SQL_FIND_BY_ID)
            .param(id).query(Currency.class).optional();
    }

    /**
     * Find a currency by ISO3 code.
     */
    public Optional<Currency> findByIso3(String iso3) {
        return jdbcClient.sql(SQL_FIND_BY_ISO3)
            .param(iso3).query(Currency.class).optional();
    }

    /**
     * Find a currency by PID.
     */
    public Optional<Currency> findByPid(String pid) {
        return jdbcClient.sql(SQL_FIND_BY_PID)
            .param(pid).query(Currency.class).optional();
    }

    /**
     * Find a currency by name.
     */
    public Optional<Currency> findByName(String name) {
        return jdbcClient.sql(SQL_FIND_BY_NAME)
            .param(name).query(Currency.class).optional();
    }

    /**
     * Get all currencies.
     */
    // public List<Currency> findAll() {
    //     return jdbcClient.sql(SQL_FIND_ALL)
    //         .query(Currency.class).list();
    // }

    /**
     * Get all currencies with pagination.
     */
    public List<Currency> findAll(int page, int size) {
        int offset = page * size;
        return jdbcClient.sql(SQL_FIND_ALL_PAGINATED)
            .param(size)
            .param(offset)
            .query(Currency.class)
            .list();
    }

    /**
     * Get total count of currencies.
     */
    public long count() {
        Long result = jdbcClient.sql(SQL_COUNT)
            .query(Long.class).single();
        return result != null ? result : 0;
    }

    /**
     * Save a new currency.
     */
    public Currency save(Currency currency) {
        jdbcClient.sql(SQL_INSERT)
            .param(currency.getId())
            .param(currency.getPid())
            .param(currency.getName())
            .param(currency.getIso3())
            .param(currency.getCode())
            .param(currency.getSymbol())
            .param(currency.getCreatedAt())
            .param(currency.getUpdatedAt())
            .update();
        return currency;
    }

    /**
     * Update an existing currency.
     */
    public Currency update(Currency currency) {
        jdbcClient.sql(SQL_UPDATE)
            .param(currency.getPid())
            .param(currency.getName())
            .param(currency.getIso3())
            .param(currency.getCode())
            .param(currency.getSymbol())
            .param(currency.getUpdatedAt())
            .param(currency.getId())
            .update();
        return currency;
    }

    /**
     * Delete a currency by its ID.
     */
    public void deleteById(String id) {
        jdbcClient.sql(SQL_DELETE)
            .param(id).update();
    }

    /**
     * Check if a currency exists by ID.
     */
    public boolean existsById(String id) {
        Integer result = jdbcClient.sql(SQL_EXISTS)
            .param(id)
            .query(Integer.class)
            .optional()
            .orElse(null);
        return result != null;
    }

    /**
     * Find countries associated with the given currency id.
     */
    public List<Country> findCountriesByCurrencyId(String currencyId) {
        return jdbcClient.sql(SQL_FIND_COUNTRIES_BY_CURRENCY_ID)
            .param(currencyId)
            .query(Country.class)
            .list();
    }

    /**
     * Find countries associated with the given currency pid.
     */
    public List<Country> findCountriesByCurrencyPid(String currencyPid) {
        return jdbcClient.sql(SQL_FIND_COUNTRIES_BY_CURRENCY_PID)
            .param(currencyPid)
            .query(Country.class)
            .list();
    }

    /**
     * Find countries associated with the given currency ISO3 code.
     */
    public List<Country> findCountriesByCurrencyIso3(String iso3) {
        return jdbcClient.sql(SQL_FIND_COUNTRIES_BY_CURRENCY_ISO3)
            .param(iso3)
            .query(Country.class)
            .list();
    }

    /**
     * Create association between currency and country.
     */
    public boolean addCountryToCurrency(String countryId, String currencyId) {
        return jdbcClient.sql(SQL_ADD_COUNTRY)
            .param(countryId)
            .param(currencyId)
            .query(String.class)
            .optional()
            .isPresent();
    }

    /**
     * Remove association between currency and country.
     */
    public void removeCountryFromCurrency(String countryId, String currencyId) {
        jdbcClient.sql(SQL_REMOVE_COUNTRY)
            .param(countryId)
            .param(currencyId)
            .update();
    }
}
