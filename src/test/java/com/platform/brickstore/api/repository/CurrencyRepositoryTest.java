
package com.platform.brickstore.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.platform.brickstore.api.domain.entity.Currency;
import com.platform.brickstore.api.utility.UUIDv7;

/**
 * Integration tests for CurrencyRepository using PostgreSQL.
 */
@SpringBootTest
@ActiveProfiles("test")
@Import(TestFlywayConfig.class)
@org.junit.jupiter.api.TestInstance(org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CurrencyRepository Tests")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yaml", properties = {"spring.flyway.clean-disabled=false", "spring.flyway.cleanDisabled=false"})


class CurrencyRepositoryTest {
    static {
        System.setProperty("flyway.cleanDisabled", "false");
        System.setProperty("spring.flyway.clean-disabled", "false");
    }
    @Autowired
    private Flyway flyway;
    // @Autowired
    // private javax.sql.DataSource ds;
    @Autowired
    private CurrencyRepository currencyRepository;
    private Currency testCurrency;


    @BeforeEach
    void cleanMigrateAndSetUp() {
        System.setProperty("flyway.cleanDisabled", "false");
        System.setProperty("spring.flyway.clean-disabled", "false");
        try {
            flyway.clean();
            flyway.migrate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Flyway clean/migrate failed", e);
        }
        testCurrency = Currency.builder()
            .id(UUIDv7.generate().toString())
            .pid(UUIDv7.generate().toString())
            .name("Test Dollar")
            .iso3("TST")
            .code((short) 999)
            .symbol("T$")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Save and retrieve currency by ID")
    void testSaveAndFindById() {
        // Arrange
        currencyRepository.save(testCurrency);

        // Act
        Optional<Currency> found = currencyRepository.findById(testCurrency.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(testCurrency.getName(), found.get().getName());
        assertEquals(testCurrency.getIso3(), found.get().getIso3());
    }

    @Test
    @DisplayName("Find currency by PID")
    void testFindByPid() {
        // Arrange
        currencyRepository.save(testCurrency);

        // Act
        Optional<Currency> found = currencyRepository.findByPid(testCurrency.getPid());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(testCurrency.getName(), found.get().getName());
    }

    @Test
    @DisplayName("Find currency by ISO3 code")
    void testFindByIso3() {
        // Arrange
        currencyRepository.save(testCurrency);

        // Act
        Optional<Currency> found = currencyRepository.findByIso3(testCurrency.getIso3());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(testCurrency.getName(), found.get().getName());
    }

    @Test
    @DisplayName("Find currency by name")
    void testFindByName() {
        // Arrange
        currencyRepository.save(testCurrency);

        // Act
        Optional<Currency> found = currencyRepository.findByName(testCurrency.getName());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(testCurrency.getCode(), found.get().getCode());
    }

    @Test
    @DisplayName("Find all currencies")
    void testFindAll() {
        // Arrange
        Currency currency2 = Currency.builder()
            .id(UUIDv7.generate().toString())
            .pid(UUIDv7.generate().toString())
            .name("Example Peso")
            .iso3("EXM")
            .code((short) 998)
            .symbol("E$")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        currencyRepository.save(testCurrency);
        currencyRepository.save(currency2);

        // Act
        List<Currency> currencies = currencyRepository.findAll();

        // Assert
        assertEquals(2, currencies.size());
    }

    @Test
    @DisplayName("Count currencies")
    void testCount() {
        // Arrange
        currencyRepository.save(testCurrency);

        // Act
        long count = currencyRepository.count();

        // Assert
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Update currency")
    void testUpdate() {
        // Arrange
        currencyRepository.save(testCurrency);
        testCurrency.setName("United States Dollar");
        testCurrency.setUpdatedAt(LocalDateTime.now());

        // Act
        currencyRepository.update(testCurrency);
        Optional<Currency> updated = currencyRepository.findById(testCurrency.getId());

        // Assert
        assertTrue(updated.isPresent());
        assertEquals("United States Dollar", updated.get().getName());
    }

    @Test
    @DisplayName("Delete currency")
    void testDelete() {
        // Arrange
        currencyRepository.save(testCurrency);

        // Act
        currencyRepository.deleteById(testCurrency.getId());
        Optional<Currency> deleted = currencyRepository.findById(testCurrency.getId());

        // Assert
        assertFalse(deleted.isPresent());
    }

    @Test
    @DisplayName("Check if currency exists")
    void testExistsById() {
        // Arrange
        currencyRepository.save(testCurrency);

        // Act
        boolean exists = currencyRepository.existsById(testCurrency.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Check if non-existent currency exists")
    void testExistsByIdNotFound() {
        // Act
        boolean exists = currencyRepository.existsById("nonexistent-id");

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Find all with pagination")
    void testFindAllPaginated() {
        // Arrange
        for (int i = 0; i < 5; i++) {
            Currency c = Currency.builder()
                .id(UUIDv7.generate().toString())
                .pid(UUIDv7.generate().toString())
                .name("CurrencyTest " + i)
                .iso3("T0" + i)
                .code((short) (900 + i))
                .symbol("T$" + i)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            currencyRepository.save(c);
        }

        // Act
        List<Currency> page0 = currencyRepository.findAll(0, 2);
        List<Currency> page1 = currencyRepository.findAll(1, 2);

        // Assert
        assertEquals(2, page0.size());
        assertEquals(2, page1.size());
    }
}
