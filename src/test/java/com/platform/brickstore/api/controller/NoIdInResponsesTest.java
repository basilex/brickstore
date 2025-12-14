package com.platform.brickstore.api.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.platform.brickstore.api.repository.TestFlywayConfig;

/**
 * Integration test asserting internal DB `id` fields are not exposed in API responses.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestFlywayConfig.class)
@DisplayName("API responses do not expose internal id fields")
public class NoIdInResponsesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    void cleanMigrate() {
        // Ensure test DB has the expected schema/migrations
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void countriesDoNotContainInternalId() throws Exception {
        var result = mockMvc.perform(get("/api/v1/countries"))
            .andExpect(status().isOk())
            .andReturn();

        String body = result.getResponse().getContentAsString();
        // The response must not contain the internal PK field name `id` anywhere
        assertFalse(body.contains("\"id\":"), "Response must not expose internal 'id' field");
    }
}
