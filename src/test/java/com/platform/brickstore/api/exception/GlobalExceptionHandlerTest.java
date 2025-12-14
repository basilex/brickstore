package com.platform.brickstore.api.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for GlobalExceptionHandler.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("NotFoundException returns 404 with errorCode")
    void testNotFoundExceptionHandling() throws Exception {
        // Act & Assert - accessing non-existent country
        mockMvc.perform(get("/api/v1/countries/nonexistent-pid"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.details.errorCode").exists());
    }

    @Test
    @DisplayName("Validation error returns 400")
    void testValidationErrorHandling() throws Exception {
        // Act & Assert - missing required field in request body
        mockMvc.perform(post("/api/v1/countries")
            .contentType("application/json")
            .content("{\"iso2\":\"US\",\"iso3\":\"USA\",\"code\":840}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Response includes timestamp in error")
    void testErrorResponseIncludesTimestamp() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/countries/invalid"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("404 Not Found for non-existent currency")
    void testCurrencyNotFoundHandling() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/currencies/nonexistent-pid"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.details.errorCode").value("CURRENCY_NOT_FOUND"));
    }
}
