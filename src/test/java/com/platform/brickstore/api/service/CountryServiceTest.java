package com.platform.brickstore.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.platform.brickstore.api.domain.entity.Country;
import com.platform.brickstore.api.exception.ErrorCode;
import com.platform.brickstore.api.exception.NotFoundException;
import com.platform.brickstore.api.repository.CountryRepository;
import com.platform.brickstore.api.repository.CurrencyRepository;
import com.platform.brickstore.api.utility.UUIDv7;

/**
 * Unit tests for CountryService using Mockito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CountryService Tests")
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CountryService countryService;

    private Country testCountry;
    private String testId;
    private String testPid;

    @BeforeEach
    void setUp() {
        testId = UUIDv7.generate().toString();
        testPid = UUIDv7.generate().toString();
        
        testCountry = Country.builder()
            .id(testId)
            .pid(testPid)
            .name("United States")
            .iso2("US")
            .iso3("USA")
            .code((short) 840)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Get country by PID successfully")
    void testGetByPidSuccess() {
        // Arrange
        when(countryRepository.findByPid(testPid)).thenReturn(Optional.of(testCountry));

        // Act
        Country result = countryService.getByPid(testPid);

        // Assert
        assertNotNull(result);
        assertEquals(testCountry.getName(), result.getName());
        verify(countryRepository, times(1)).findByPid(testPid);
    }

    @Test
    @DisplayName("Get country by PID throws NotFoundException when not found")
    void testGetByPidNotFound() {
        // Arrange
        when(countryRepository.findByPid(testPid)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            countryService.getByPid(testPid);
        });
        
        assertEquals(ErrorCode.COUNTRY_NOT_FOUND, exception.getErrorCode());
        verify(countryRepository, times(1)).findByPid(testPid);
    }

    @Test
    @DisplayName("Get country by ISO2 successfully")
    void testGetByIso2Success() {
        // Arrange
        when(countryRepository.findByIso2("US")).thenReturn(Optional.of(testCountry));

        // Act
        Country result = countryService.getByIso2("US");

        // Assert
        assertNotNull(result);
        assertEquals("United States", result.getName());
        verify(countryRepository, times(1)).findByIso2("US");
    }

    @Test
    @DisplayName("Get country by ISO2 throws NotFoundException when not found")
    void testGetByIso2NotFound() {
        // Arrange
        when(countryRepository.findByIso2("XX")).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            countryService.getByIso2("XX");
        });
        
        assertEquals(ErrorCode.COUNTRY_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Create country successfully")
    void testCreateSuccess() {
        // Arrange
        Country newCountry = Country.builder()
            .name("Canada")
            .iso2("CA")
            .iso3("CAN")
            .code((short) 124)
            .build();
        
        when(countryRepository.save(any(Country.class))).thenAnswer(invocation -> {
            Country country = invocation.getArgument(0);
            return country;
        });

        // Act
        Country result = countryService.create(newCountry);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPid());
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
        verify(countryRepository, times(1)).save(any(Country.class));
    }

    @Test
    @DisplayName("Update country successfully")
    void testUpdateSuccess() {
        // Arrange
        Country updateData = Country.builder()
            .name("United States of America")
            .iso2("US")
            .iso3("USA")
            .code((short) 840)
            .build();
        
        when(countryRepository.findById(testId)).thenReturn(Optional.of(testCountry));
        when(countryRepository.update(any(Country.class))).thenAnswer(invocation -> {
            Country country = invocation.getArgument(0);
            return country;
        });

        // Act
        Country result = countryService.update(testId, updateData);

        // Assert
        assertNotNull(result);
        assertEquals("United States of America", result.getName());
        verify(countryRepository, times(1)).findById(testId);
        verify(countryRepository, times(1)).update(any(Country.class));
    }

    @Test
    @DisplayName("Update country throws NotFoundException when country not found")
    void testUpdateNotFound() {
        // Arrange
        when(countryRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            countryService.update(testId, testCountry);
        });
        
        assertEquals(ErrorCode.COUNTRY_NOT_FOUND, exception.getErrorCode());
        verify(countryRepository, times(1)).findById(testId);
        verify(countryRepository, never()).update(any());
    }

    @Test
    @DisplayName("Delete country successfully")
    void testDeleteSuccess() {
        // Arrange
        when(countryRepository.existsById(testId)).thenReturn(true);

        // Act
        countryService.delete(testId);

        // Assert
        verify(countryRepository, times(1)).existsById(testId);
        verify(countryRepository, times(1)).deleteById(testId);
    }

    @Test
    @DisplayName("Delete country throws NotFoundException when country not found")
    void testDeleteNotFound() {
        // Arrange
        when(countryRepository.existsById(testId)).thenReturn(false);

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            countryService.delete(testId);
        });
        
        assertEquals(ErrorCode.COUNTRY_NOT_FOUND, exception.getErrorCode());
        verify(countryRepository, times(1)).existsById(testId);
        verify(countryRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Add currency to country successfully")
    void testAddCurrencyToCountrySuccess() {
        // Arrange
        String currencyId = UUIDv7.generate().toString();
        when(countryRepository.existsById(testId)).thenReturn(true);
        when(currencyRepository.existsById(currencyId)).thenReturn(true);
        when(countryRepository.addCurrencyToCountry(testId, currencyId)).thenReturn(true);

        // Act
        boolean result = countryService.addCurrencyToCountry(testId, currencyId);

        // Assert
        assertTrue(result);
        verify(countryRepository, times(1)).existsById(testId);
        verify(currencyRepository, times(1)).existsById(currencyId);
        verify(countryRepository, times(1)).addCurrencyToCountry(testId, currencyId);
    }

    @Test
    @DisplayName("Add currency to non-existent country throws NotFoundException")
    void testAddCurrencyToCountryNotFound() {
        // Arrange
        String currencyId = UUIDv7.generate().toString();
        when(countryRepository.existsById(testId)).thenReturn(false);

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            countryService.addCurrencyToCountry(testId, currencyId);
        });
        
        assertEquals(ErrorCode.COUNTRY_NOT_FOUND, exception.getErrorCode());
    }
}

