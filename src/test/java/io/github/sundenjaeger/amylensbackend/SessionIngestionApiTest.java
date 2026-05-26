// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionIngestionApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void confidenceScoreAboveMax_shouldReturn400() throws Exception {
        String payload = """
                {
                    "deviceSsaid": "test-device",
                    "userName": "Researcher A",
                    "variety": "IR64",
                    "amyloseClass": "Intermediate",
                    "confidenceScore": 1.01,
                    "capturedAt": "2026-05-25T09:00:00.000Z",
                    "submittedAt": "2026-05-25T09:01:00.000Z",
                    "trialStage": "OYT",
                    "season": "Dry",
                    "imageHash": "abc123"
                }
                """;

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    void confidenceScoreBelowMin_shouldReturn400() throws Exception {
        String payload = """
                {
                    "deviceSsaid": "test-device",
                    "userName": "Researcher A",
                    "variety": "IR64",
                    "amyloseClass": "Intermediate",
                    "confidenceScore": -0.01,
                    "capturedAt": "2026-05-25T09:00:00.000Z",
                    "submittedAt": "2026-05-25T09:01:00.000Z",
                    "trialStage": "OYT",
                    "season": "Dry",
                    "imageHash": "abc123"
                }
                """;

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    void missingRequiredField_shouldReturn400() throws Exception {
        String payload = """
                {
                    "deviceSsaid": "test-device",
                    "confidenceScore": 0.91
                }
                """;

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnprocessableContent());
    }
}
