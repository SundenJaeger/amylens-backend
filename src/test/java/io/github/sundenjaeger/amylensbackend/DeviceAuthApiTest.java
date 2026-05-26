// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend;

import io.github.sundenjaeger.amylensbackend.dto.DeviceAuthRequest;
import io.github.sundenjaeger.amylensbackend.dto.DeviceRegistration;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeviceAuthApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void unregisteredDevice_shouldReturn403() throws Exception {
        DeviceAuthRequest request = new DeviceAuthRequest("non-existent-ssaid");

        mockMvc.perform(post("/api/devices/auth")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void registeredButPendingDevice_shouldReturn202() throws Exception {
        DeviceRegistration reg = new DeviceRegistration("pending-device", "Test Tablet");
        mockMvc.perform(post("/api/devices/register")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(reg)));

        DeviceAuthRequest request = new DeviceAuthRequest("pending-device");
        mockMvc.perform(post("/api/devices/auth")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
