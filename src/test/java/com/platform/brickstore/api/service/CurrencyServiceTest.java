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

import com.platform.brickstore.api.domain.entity.Currency;
import com.platform.brickstore.api.exception.ErrorCode;
import com.platform.brickstore.api.exception.NotFoundException;
import com.platform.brickstore.api.repository.CountryRepository;
import com.platform.brickstore.api.repository.CurrencyRepository;
import com.platform.brickstore.api.utility.UUIDv7;

/**
 * Unit tests for CurrencyService using Mockito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CurrencyService Tests")
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CurrencyService currencyService;

    private Currency testCurrency;
    private String testId;
    private String testPid;

    @BeforeEach
    void setUp() {
        testId = UUIDv7.generate().toString();
        testPid = UUIDv7.generate().toString();
        
        testCurrency = Currency.builder()
            .id(testId)
            .pid(testPid)
            .name("US Dollar")
            .iso3("USD")
            .code((short) 840)
            .symbol("$")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Get currency by PID successfully")
    void testGetByPidSuccess() {
        // Arrange
        when(currencyRepository.findByPid(testPid)).thenReturn(Optional.of(testCurrency));

        // Act
        Currency result = currencyService.getByPid(testPid);

        // Assert
        assertNotNull(result);
        assertEquals(testCurrency.getName(), result.getName());
        verify(currencyRepository, times(1)).findByPid(testPid);
    }

    @Test
    @DisplayName("Get currency by PID throws NotFoundException when not found")
    void testGetByPidNotFound() {
        // Arrange
        when(currencyRepository.findByPid(testPid)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            currencyService.getByPid(testPid);
        });
        
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, exception.getErrorCode());
        verify(currencyRepository, times(1)).findByPid(testPid);
    }

    @Test
    @DisplayName("Get currency by ISO3 successfully")
    void testGetByIso3Success() {
        // Arrange
        when(currencyRepository.findByIso3("USD")).thenReturn(Optional.of(testCurrency));

        // Act
        Currency result = currencyService.getByIso3("USD");

        // Assert
        assertNotNull(result);
        assertEquals("US Dollar", result.getName());
        verify(currencyRepository, times(1)).findByIso3("USD");
    }

    @Test
    @DisplayName("Get currency by ISO3 throws NotFoundException when not found")
    void testGetByIso3NotFound() {
        // Arrange
        when(currencyRepository.findByIso3("XXX")).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            currencyService.getByIso3("XXX");
        });
        
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Create currency successfully")
    void testCreateSuccess() {
        // Arrange
        Currency newCurrency = Currency.builder()
            .name("Euro")
            .iso3("EUR")
            .code((short) 978)
            .symbol("€")
            .build();
        
        when(currencyRepository.save(any(Currency.class))).thenAnswer(invocation -> {
            Currency currency = invocation.getArgument(0);
            return currency;
        });

        // Act
        Currency result = currencyService.create(newCurrency);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPid());
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
        verify(currencyRepository, times(1)).save(any(Currency.class));
    }

    @Test
    @DisplayName("Update currency successfully")
    void testUpdateSuccess() {
        // Arrange
        Currency updateData = Currency.builder()
            .name("United States Dollar")
            .iso3("USD")
            .code((short) 840)
            .symbol("$")
            .build();
        
        when(currencyRepository.findById(testId)).thenReturn(Optional.of(testCurrency));
        when(currencyRepository.update(any(Currency.class))).thenAnswer(invocation -> {
            Currency currency = invocation.getArgument(0);
            return currency;
        });

        // Act
        Currency result = currencyService.update(testId, updateData);

        // Assert
        assertNotNull(result);
        assertEquals("United States Dollar", result.getName());
        verify(currencyRepository, times(1)).findById(testId);
        verify(currencyRepository, times(1)).update(any(Currency.class));
    }

    @Test
    @DisplayName("Update currency throws NotFoundException when currency not found")
    void testUpdateNotFound() {
        // Arrange
        when(currencyRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            currencyService.update(testId, testCurrency);
        });
        
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, exception.getErrorCode());
        verify(currencyRepository, times(1)).findById(testId);
        verify(currencyRepository, never()).update(any());
    }

    @Test
    @DisplayName("Delete currency successfully")
    void testDeleteSuccess() {
        // Arrange
        when(currencyRepository.existsById(testId)).thenReturn(true);

        // Act
        currencyService.delete(testId);

        // Assert
        verify(currencyRepository, times(1)).existsById(testId);
        verify(currencyRepository, times(1)).deleteById(testId);
    }

    @Test
    @DisplayName("Delete currency throws NotFoundException when currency not found")
    void testDeleteNotFound() {
        // Arrange
        when(currencyRepository.existsById(testId)).thenReturn(false);

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            currencyService.delete(testId);
        });
        
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, exception.getErrorCode());
        verify(currencyRepository, times(1)).existsById(testId);
        verify(currencyRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Add country to currency successfully")
    void testAddCountryToCurrencySuccess() {
        // Arrange
        String countryId = UUIDv7.generate().toString();
        when(currencyRepository.existsById(testId)).thenReturn(true);
        when(countryRepository.existsById(countryId)).thenReturn(true);
        when(currencyRepository.addCountryToCurrency(countryId, testId)).thenReturn(true);

        // Act
        boolean result = currencyService.addCountryToCurrency(countryId, testId);

        // Assert
        assertTrue(result);
        verify(currencyRepository, times(1)).existsById(testId);
        verify(countryRepository, times(1)).existsById(countryId);
        verify(currencyRepository, times(1)).addCountryToCurrency(countryId, testId);
    }

    @Test
    @DisplayName("Add country to non-existent currency throws NotFoundException")
    void testAddCountryToCurrencyNotFound() {
        // Arrange
        String countryId = UUIDv7.generate().toString();
        when(currencyRepository.existsById(testId)).thenReturn(false);

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            currencyService.addCountryToCurrency(countryId, testId);
        });
        
        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, exception.getErrorCode());
    }
}
