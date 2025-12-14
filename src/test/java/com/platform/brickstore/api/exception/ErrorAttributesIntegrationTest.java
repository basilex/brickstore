package com.platform.brickstore.api.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("/error endpoint integration")
public class ErrorAttributesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Non-existent path returns ProblemDetail-like /error with type, instance and requestId")
    void errorEndpointReturnsProblemDetailLikeAttributes() throws Exception {
        String rid = "test-request-id-123";

        mockMvc.perform(get("/this-path-does-not-exist").header("X-Request-Id", rid))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.type").value("about:blank"))
            .andExpect(jsonPath("$.instance").value("urn:uuid:" + rid))
            .andExpect(jsonPath("$.requestId").value(rid));
    }
}
