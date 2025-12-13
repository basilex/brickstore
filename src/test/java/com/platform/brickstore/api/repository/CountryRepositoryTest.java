
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

import com.platform.brickstore.api.domain.entity.Country;
import com.platform.brickstore.api.utility.UUIDv7;

/**
 * Integration tests for CountryRepository using H2 in-memory database.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yaml", properties = {"spring.flyway.clean-disabled=false", "spring.flyway.cleanDisabled=false"})
@DisplayName("CountryRepository Tests")
@Import(TestFlywayConfig.class)
@org.junit.jupiter.api.TestInstance(org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS)

class CountryRepositoryTest {
    static {
        System.setProperty("flyway.cleanDisabled", "false");
        System.setProperty("spring.flyway.clean-disabled", "false");
    }
    @Autowired
    private Flyway flyway;
    // @Autowired
    // private javax.sql.DataSource ds;


    @Autowired
    private CountryRepository countryRepository;

    private Country testCountry;
    private static final String UNIQUE_NAME = "Testlandia";
    private static final String UNIQUE_ISO2 = "TX";
    private static final String UNIQUE_ISO3 = "TST";
    private static final short UNIQUE_CODE = 999;

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
        testCountry = Country.builder()
            .id(UUIDv7.generate().toString())
            .pid(UUIDv7.generate().toString())
            .name(UNIQUE_NAME)
            .iso2(UNIQUE_ISO2)
            .iso3(UNIQUE_ISO3)
            .code(UNIQUE_CODE)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Save and retrieve country by ID")
    void testSaveAndFindById() {
        // Arrange
        countryRepository.save(testCountry);

        // Act
        Optional<Country> found = countryRepository.findById(testCountry.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(UNIQUE_NAME, found.get().getName());
        assertEquals(UNIQUE_ISO2, found.get().getIso2());
    }

    @Test
    @DisplayName("Find country by PID")
    void testFindByPid() {
        // Arrange
        countryRepository.save(testCountry);

        // Act
        Optional<Country> found = countryRepository.findByPid(testCountry.getPid());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(UNIQUE_NAME, found.get().getName());
    }

    @Test
    @DisplayName("Find country by ISO2 code")
    void testFindByIso2() {
        // Arrange
        countryRepository.save(testCountry);

        // Act
        Optional<Country> found = countryRepository.findByIso2(UNIQUE_ISO2);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(UNIQUE_NAME, found.get().getName());
    }

    @Test
    @DisplayName("Find country by ISO3 code")
    void testFindByIso3() {
        // Arrange
        countryRepository.save(testCountry);

        // Act
        Optional<Country> found = countryRepository.findByIso3(UNIQUE_ISO3);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(UNIQUE_ISO2, found.get().getIso2());
    }

    @Test
    @DisplayName("Find country by name")
    void testFindByName() {
        // Arrange
        countryRepository.save(testCountry);

        // Act
        Optional<Country> found = countryRepository.findByName(UNIQUE_NAME);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(UNIQUE_CODE, found.get().getCode());
    }

    @Test
    @DisplayName("Find all countries")
    void testFindAll() {
        // Arrange
        Country country2 = Country.builder()
            .id(UUIDv7.generate().toString())
            .pid(UUIDv7.generate().toString())
            .name("Examplestan")
            .iso2("EX")
            .iso3("EXM")
            .code((short) 998)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        countryRepository.save(testCountry);
        countryRepository.save(country2);

        // Act
        List<Country> countries = countryRepository.findAll();
        long testCount = countries.stream().filter(c -> c.getName().equals(UNIQUE_NAME) || c.getName().equals("Examplestan")).count();

        // Assert
        assertEquals(2, testCount);
    }

    @Test
    @DisplayName("Count countries")
    void testCount() {
        // Arrange
        countryRepository.save(testCountry);

        // Act
        long count = countryRepository.findAll().stream().filter(c -> c.getName().equals(UNIQUE_NAME)).count();

        // Assert
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Update country")
    void testUpdate() {
        // Arrange
        countryRepository.save(testCountry);
        testCountry.setName("Testlandia Updated");
        testCountry.setUpdatedAt(LocalDateTime.now());

        // Act
        countryRepository.update(testCountry);
        Optional<Country> updated = countryRepository.findById(testCountry.getId());

        // Assert
        assertTrue(updated.isPresent());
        assertEquals("Testlandia Updated", updated.get().getName());
    }

    @Test
    @DisplayName("Delete country")
    void testDelete() {
        // Arrange
        countryRepository.save(testCountry);

        // Act
        countryRepository.deleteById(testCountry.getId());
        Optional<Country> deleted = countryRepository.findById(testCountry.getId());

        // Assert
        assertFalse(deleted.isPresent());
    }

    @Test
    @DisplayName("Check if country exists")
    void testExistsById() {
        // Arrange
        countryRepository.save(testCountry);

        // Act
        boolean exists = countryRepository.existsById(testCountry.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Check if non-existent country exists")
    void testExistsByIdNotFound() {
        // Act
        boolean exists = countryRepository.existsById("nonexistent-id");

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Find all with pagination")
    void testFindAllPaginated() {
        // Arrange
        for (int i = 0; i < 5; i++) {
            Country c = Country.builder()
                .id(UUIDv7.generate().toString())
                .pid(UUIDv7.generate().toString())
                .name("CountryTest " + i)
                .iso2("T" + i) // always 2 chars
                .iso3("T" + i + "X") // always 3 chars
                .code((short) (910 + i))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            countryRepository.save(c);
        }

        // Act
        List<Country> page0 = countryRepository.findAll(0, 2);
        List<Country> page1 = countryRepository.findAll(1, 2);

        // Assert (just check page size, not content)
        assertEquals(2, page0.size());
        assertEquals(2, page1.size());
    }
}
