package com.platform.brickstore.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.platform.brickstore.api.domain.entity.Country;
import com.platform.brickstore.api.domain.entity.Currency;

import lombok.AllArgsConstructor;

/**
 * Repository for Country entity using JdbcClient.
 */
@Repository
@AllArgsConstructor
public class CountryRepository {

    private static final String SQL_FIND_BY_ID = """
        SELECT id, pid, name, iso2, iso3, code, created_at, updated_at
          FROM country WHERE id = ?
    """;

    private static final String SQL_FIND_BY_ISO2 = """
        SELECT id, pid, name, iso2, iso3, code, created_at, updated_at
          FROM country WHERE iso2 = ?
    """;

    private static final String SQL_FIND_BY_PID = """
        SELECT id, pid, name, iso2, iso3, code, created_at, updated_at
          FROM country WHERE pid = ?
    """;

    private static final String SQL_FIND_BY_ISO3 = """
        SELECT id, pid, name, iso3, iso2, code, created_at, updated_at
          FROM country WHERE iso3 = ?
    """;

    private static final String SQL_FIND_BY_NAME = """
        SELECT id, pid, name, iso2, iso3, code, created_at, updated_at
          FROM country WHERE name = ?
    """;

    private static final String SQL_FIND_ALL = """
        SELECT id, pid, name, iso2, iso3, code, created_at, updated_at
          FROM country ORDER BY name
    """;

    private static final String SQL_FIND_ALL_PAGINATED = """
        SELECT id, pid, name, iso2, iso3, code, created_at, updated_at
          FROM country ORDER BY name LIMIT ? OFFSET ?
    """;

    private static final String SQL_COUNT = "SELECT COUNT(*) FROM country";

    private static final String SQL_INSERT = """
        INSERT INTO country (id, pid, name, iso2, iso3, code, created_at, updated_at)
          VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String SQL_UPDATE = """
        UPDATE country SET pid = ?, name = ?, iso2 = ?, iso3 = ?, code = ?, updated_at = ?
         WHERE id = ?
    """;

    private static final String SQL_DELETE = "DELETE FROM country WHERE id = ?";

    private static final String SQL_EXISTS = "SELECT 1 FROM country WHERE id = ? LIMIT 1";

    private static final String SQL_FIND_CURRENCIES_BY_COUNTRY_ID = """
        SELECT cur.id, cur.pid, cur.name, cur.iso3, cur.code, cur.symbol, cur.created_at, cur.updated_at
          FROM currency cur
          JOIN country_currency cc ON cur.id = cc.currency_id
         WHERE cc.country_id = ? ORDER BY cur.name
    """;

    private static final String SQL_FIND_CURRENCIES_BY_COUNTRY_PID = """
        SELECT cur.id, cur.pid, cur.name, cur.iso3, cur.code, cur.symbol, cur.created_at, cur.updated_at
          FROM currency cur
          JOIN country_currency cc ON cur.id = cc.currency_id
          JOIN country c ON c.id = cc.country_id
         WHERE c.pid = ? ORDER BY cur.name
    """;

    private static final String SQL_FIND_CURRENCIES_BY_COUNTRY_ISO2 = """
        SELECT cur.id, cur.pid, cur.name, cur.iso3, cur.code, cur.symbol, cur.created_at, cur.updated_at
          FROM currency cur
          JOIN country_currency cc ON cur.id = cc.currency_id
          JOIN country c ON c.id = cc.country_id
         WHERE c.iso2 = ? ORDER BY cur.name
    """;

    private static final String SQL_ADD_CURRENCY = """
        INSERT INTO country_currency (country_id, currency_id)
          VALUES (?, ?)
          ON CONFLICT (country_id, currency_id) DO NOTHING RETURNING id
    """;

    private static final String SQL_REMOVE_CURRENCY = """
        DELETE FROM country_currency
         WHERE country_id = ? AND currency_id = ?
    """;

    private final JdbcClient jdbcClient;

    /**
     * Find a country by its ID.
     */
    public Optional<Country> findById(String id) {
        return jdbcClient.sql(SQL_FIND_BY_ID)
            .param(id)
            .query(Country.class)
            .optional();
    }

    /**
     * Find a country by ISO2 code.
     */
    public Optional<Country> findByIso2(String iso2) {
        return jdbcClient.sql(SQL_FIND_BY_ISO2)
            .param(iso2)
            .query(Country.class)
            .optional();
    }

    /**
     * Find a country by PID.
     */
    public Optional<Country> findByPid(String pid) {
        return jdbcClient.sql(SQL_FIND_BY_PID)
            .param(pid)
            .query(Country.class)
            .optional();
    }

    /**
     * Find a country by ISO3 code.
     */
    public Optional<Country> findByIso3(String iso3) {
        return jdbcClient.sql(SQL_FIND_BY_ISO3)
            .param(iso3)
            .query(Country.class)
            .optional();
    }

    /**
     * Find a country by name.
     */
    public Optional<Country> findByName(String name) {
        return jdbcClient.sql(SQL_FIND_BY_NAME)
            .param(name)
            .query(Country.class)
            .optional();
    }

    /**
     * Get all countries.
     */
    // public List<Country> findAll() {
    //     return jdbcClient.sql(SQL_FIND_ALL)
    //         .query(Country.class)
    //         .list();
    // }

    /**
     * Get all countries with pagination.
     */
    public List<Country> findAll(int page, int size) {
        int offset = page * size;
        return jdbcClient.sql(SQL_FIND_ALL_PAGINATED)
            .param(size)
            .param(offset)
            .query(Country.class)
            .list();
    }

    /**
     * Get total count of countries.
     */
    public long count() {
        Long result = jdbcClient.sql(SQL_COUNT)
            .query(Long.class)
            .single();
        return result != null ? result : 0;
    }

    /**
     * Save a new country.
     */
    public Country save(Country country) {
        jdbcClient.sql(SQL_INSERT)
            .param(country.getId())
            .param(country.getPid())
            .param(country.getName())
            .param(country.getIso2())
            .param(country.getIso3())
            .param(country.getCode())
            .param(country.getCreatedAt())
            .param(country.getUpdatedAt())
            .update();
        return country;
    }

    /**
     * Update an existing country.
     */
    public Country update(Country country) {
        jdbcClient.sql(SQL_UPDATE)
            .param(country.getPid())
            .param(country.getName())
            .param(country.getIso2())
            .param(country.getIso3())
            .param(country.getCode())
            .param(country.getUpdatedAt())
            .param(country.getId())
            .update();
        return country;
    }

    /**
     * Delete a country by its ID.
     */
    public void deleteById(String id) {
        jdbcClient.sql(SQL_DELETE)
            .param(id)
            .update();
    }

    /**
     * Check if a country exists by ID.
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
     * Find currencies associated with the given country id.
     */
    public List<Currency> findCurrenciesByCountryId(String countryId) {
        return jdbcClient.sql(SQL_FIND_CURRENCIES_BY_COUNTRY_ID)
            .param(countryId)
            .query(Currency.class)
            .list();
    }

    /**
     * Find currencies associated with the given country pid.
     */
    public List<Currency> findCurrenciesByCountryPid(String countryPid) {
        return jdbcClient.sql(SQL_FIND_CURRENCIES_BY_COUNTRY_PID)
            .param(countryPid)
            .query(Currency.class)
            .list();
    }

    /**
     * Find currencies associated with the given country ISO2 code.
     */
    public List<Currency> findCurrenciesByCountryIso2(String iso2) {
        return jdbcClient.sql(SQL_FIND_CURRENCIES_BY_COUNTRY_ISO2)
            .param(iso2)
            .query(Currency.class)
            .list();
    }

    /**
     * Create association between country and currency.
     */
    public boolean addCurrencyToCountry(String countryId, String currencyId) {
        return jdbcClient.sql(SQL_ADD_CURRENCY)
            .param(countryId)
            .param(currencyId)
            .query(String.class)
            .optional()
            .isPresent();
    }

    /**
     * Remove association between country and currency.
     */
    public void removeCurrencyFromCountry(String countryId, String currencyId) {
        jdbcClient.sql(SQL_REMOVE_CURRENCY)
            .param(countryId)
            .param(currencyId)
            .update();
    }
}
