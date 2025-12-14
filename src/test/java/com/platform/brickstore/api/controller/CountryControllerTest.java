package com.platform.brickstore.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.brickstore.api.domain.entity.Country;
import com.platform.brickstore.api.dto.CountryRequest;
import com.platform.brickstore.api.exception.ErrorCode;
import com.platform.brickstore.api.exception.NotFoundException;
import com.platform.brickstore.api.service.CountryService;
import com.platform.brickstore.api.utility.UUIDv7;

/**
 * Integration tests for CountryController using MockMvc.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("CountryController Tests")
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    @DisplayName("GET /api/v1/countries - Get all countries")
    void testGetAllCountries() throws Exception {
        // Arrange
        Country country2 = Country.builder()
            .id(UUIDv7.generate().toString())
            .pid(UUIDv7.generate().toString())
            .name("Canada")
            .iso2("CA")
            .iso3("CAN")
            .code((short) 124)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        when(countryService.getAllPaginated(0, 20)).thenReturn(Arrays.asList(testCountry, country2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/countries"))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$", hasSize(2)),
                jsonPath("$[0].pid", notNullValue()),
                jsonPath("$[0].name", is("United States")),
                jsonPath("$[1].name", is("Canada"))
            );
    }

    @Test
    @DisplayName("GET /api/v1/countries/{pid} - Get country by PID")
    void testGetCountryByPid() throws Exception {
        // Arrange
        when(countryService.getByPid(testPid)).thenReturn(testCountry);

        // Act & Assert
        mockMvc.perform(get("/api/v1/countries/{pid}", testPid))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid", is(testPid)))
            .andExpect(jsonPath("$.name", is("United States")))
            .andExpect(jsonPath("$.iso2", is("US")));
    }

    @Test
    @DisplayName("GET /api/v1/countries/{pid} - Not found returns 404")
    void testGetCountryByPidNotFound() throws Exception {
        // Arrange - stubbing for a PID that won't exist in mocked service
        String nonExistentPid = "non-existent-pid";
        when(countryService.getByPid(nonExistentPid))
            .thenThrow(new NotFoundException("Country not found", ErrorCode.COUNTRY_NOT_FOUND));

        // Act & Assert
        mockMvc.perform(get("/api/v1/countries/{pid}", nonExistentPid))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error", is("Not Found")))
            .andExpect(jsonPath("$.details.errorCode", is("COUNTRY_NOT_FOUND")));
    }

    @Test
    @DisplayName("POST /api/v1/countries - Create country")
    void testCreateCountry() throws Exception {
        // Arrange
        CountryRequest request = new CountryRequest(
            "Mexico",
            "MX",
            "MEX",
            (short) 484
        );
        
        Country newCountry = Country.builder()
            .id(UUIDv7.generate().toString())
            .pid(UUIDv7.generate().toString())
            .name("Mexico")
            .iso2("MX")
            .iso3("MEX")
            .code((short) 484)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        when(countryService.create(ArgumentMatchers.any(Country.class))).thenReturn(newCountry);

        // Act & Assert
        mockMvc.perform(post("/api/v1/countries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.pid", notNullValue()))
            .andExpect(jsonPath("$.name", is("Mexico")))
            .andExpect(jsonPath("$.iso2", is("MX")));
    }

    @Test
    @DisplayName("POST /api/v1/countries - Validation error returns 400")
    void testCreateCountryValidationError() throws Exception {
        // Arrange
        CountryRequest invalidRequest = new CountryRequest(
            "", // Empty name
            "US",
            "USA",
            (short) 840
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/countries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/v1/countries/{pid} - Update country")
    void testUpdateCountry() throws Exception {
        // Arrange
        CountryRequest request = new CountryRequest(
            "United States of America",
            "US",
            "USA",
            (short) 840
        );
        
        Country updatedCountry = Country.builder()
            .id(testId)
            .pid(testPid)
            .name("United States of America")
            .iso2("US")
            .iso3("USA")
            .code((short) 840)
            .createdAt(testCountry.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();
        
        when(countryService.getByPid(testPid)).thenReturn(testCountry);
        when(countryService.update(eq(testId), ArgumentMatchers.any(Country.class))).thenReturn(updatedCountry);

        // Act & Assert
        mockMvc.perform(put("/api/v1/countries/{pid}", testPid)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pid", is(testPid)))
            .andExpect(jsonPath("$.name", is("United States of America")));
    }

    @Test
    @DisplayName("DELETE /api/v1/countries/{pid} - Delete country")
    void testDeleteCountry() throws Exception {
        // Arrange
        when(countryService.getByPid(testPid)).thenReturn(testCountry);
        doNothing().when(countryService).delete(testId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/countries/{pid}", testPid))
            .andExpect(status().isNoContent());

        verify(countryService, times(1)).getByPid(testPid);
        verify(countryService, times(1)).delete(testId);
    }

    @Test
    @DisplayName("DELETE /api/v1/countries/{pid} - Not found returns 404")
    void testDeleteCountryNotFound() throws Exception {
        // Arrange
        String nonExistentPid = "non-existent-pid";
        when(countryService.getByPid(nonExistentPid))
            .thenThrow(new NotFoundException("Country not found", ErrorCode.COUNTRY_NOT_FOUND));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/countries/{pid}", nonExistentPid))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error", is("Not Found")))
            .andExpect(jsonPath("$.details.errorCode", is("COUNTRY_NOT_FOUND")));

        verify(countryService, never()).delete(any());
    }

    @Test
    @DisplayName("Response should not expose internal ID")
    void testResponseDoesNotExposedId() throws Exception {
        // Arrange
        when(countryService.getByPid(testPid)).thenReturn(testCountry);

        // Act & Assert
        String response = mockMvc.perform(get("/api/v1/countries/{pid}", testPid))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        
        // Verify that the response contains pid but not id
        assert(response.contains("\"pid\""));
        assert(!response.contains("\"id\""));
    }
}
