package com.platform.brickstore.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.platform.brickstore.api.domain.entity.Country;
import com.platform.brickstore.api.domain.entity.Currency;
import com.platform.brickstore.api.dto.CountryRequest;
import com.platform.brickstore.api.dto.CountryResponse;
import com.platform.brickstore.api.utility.UUIDv7;

/**
 * Integration tests for CountryMapper.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("CountryMapper Tests")
class CountryMapperTest {

    @Autowired
    private CountryMapper countryMapper;

    private Country testCountry;
    private CountryRequest testRequest;

    @BeforeEach
    void setUp() {
        String id = UUIDv7.generate().toString();
        String pid = UUIDv7.generate().toString();
        
        testCountry = Country.builder()
            .id(id)
            .pid(pid)
            .name("United States")
            .iso2("US")
            .iso3("USA")
            .code((short) 840)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        testRequest = new CountryRequest(
            "United States",
            "US",
            "USA",
            (short) 840
        );
    }

    @Test
    @DisplayName("Convert CountryRequest to Country entity")
    void testToEntity() {
        // Act
        Country result = countryMapper.toEntity(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals("United States", result.getName());
        assertEquals("US", result.getIso2());
        assertEquals("USA", result.getIso3());
        assertEquals((short) 840, result.getCode());
        assertNull(result.getId());
        assertNull(result.getPid());
    }

    @Test
    @DisplayName("Convert Country entity to CountryResponse DTO")
    void testToResponse() {
        // Act
        CountryResponse result = countryMapper.toResponse(testCountry);

        // Assert
        assertNotNull(result);
        assertEquals(testCountry.getPid(), result.pid());
        assertEquals("United States", result.name());
        assertEquals("US", result.iso2());
        assertEquals("USA", result.iso3());
        assertEquals((short) 840, result.code());
        assertNull(result.currencies());
        assertNotNull(result.createdAt());
        assertNotNull(result.updatedAt());
    }

    @Test
    @DisplayName("Convert Country entity with currencies to CountryResponse DTO")
    void testToResponseWithCurrencies() {
        // Arrange
        Currency currency1 = Currency.builder()
            .id(UUIDv7.generate().toString())
            .pid(UUIDv7.generate().toString())
            .name("US Dollar")
            .iso3("USD")
            .code((short) 840)
            .symbol("$")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        Currency currency2 = Currency.builder()
            .id(UUIDv7.generate().toString())
            .pid(UUIDv7.generate().toString())
            .name("Euro")
            .iso3("EUR")
            .code((short) 978)
            .symbol("€")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        List<Currency> currencies = Arrays.asList(currency1, currency2);

        // Act
        CountryResponse result = countryMapper.toResponse(testCountry, currencies);

        // Assert
        assertNotNull(result);
        assertEquals(testCountry.getPid(), result.pid());
        assertEquals("United States", result.name());
        assertNotNull(result.currencies());
        assertEquals(2, result.currencies().size());
        assertEquals("US Dollar", result.currencies().get(0).name());
        assertEquals("Euro", result.currencies().get(1).name());
    }

    @Test
    @DisplayName("Update Country entity from CountryRequest")
    void testUpdateEntity() {
        // Arrange
        Country existing = Country.builder()
            .id(UUIDv7.generate().toString())
            .pid(UUIDv7.generate().toString())
            .name("Old Name")
            .iso2("XX")
            .iso3("XXX")
            .code((short) 999)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        CountryRequest updateRequest = new CountryRequest(
            "Canada",
            "CA",
            "CAN",
            (short) 124
        );

        // Act
        Country result = countryMapper.updateEntity(updateRequest, existing);

        // Assert
        assertNotNull(result);
        assertEquals(existing.getId(), result.getId());
        assertEquals(existing.getPid(), result.getPid());
        assertEquals("Canada", result.getName());
        assertEquals("CA", result.getIso2());
        assertEquals("CAN", result.getIso3());
        assertEquals((short) 124, result.getCode());
    }

    @Test
    @DisplayName("CountryResponse is a record (immutable)")
    void testResponseIsRecord() {
        // Act
        CountryResponse response = countryMapper.toResponse(testCountry);

        // Assert
        assertNotNull(response);
        // Records are immutable - verify components are accessible
        assertEquals(testCountry.getPid(), response.pid());
        assertEquals(testCountry.getName(), response.name());
        // Verify no setter methods exist by checking class is a record
        assertTrue(response.getClass().isRecord());
    }
}
