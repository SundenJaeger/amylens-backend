package io.github.sundenjaeger.amylensbackend;

import io.github.sundenjaeger.amylensbackend.model.Session;
import io.github.sundenjaeger.amylensbackend.repository.SessionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;

import java.time.Instant;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExportApiTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SessionRepository sessionRepository;

    @org.junit.jupiter.api.BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @AfterEach
    public void cleanup() {
        sessionRepository.deleteAll();
    }

    @Test
    public void exportCsvContainsHeaderAndRow() {
        Session s = new Session();
        s.setDeviceSsid("test-device-01");
        s.setUserName("Tester");
        s.setVariety("IR64");
        s.setAmyloseClass("Intermediate");
        s.setConfidenceScore(0.95);
        s.setCapturedAt(Instant.now());
        s.setSubmittedAt(Instant.now());
        s.setTrialStage("OYT");
        s.setSeason("Dry");
        s.setImageHash("hash123");
        s.setNeedsReview(false);

        Session saved = sessionRepository.save(s);

        try {
            MvcResult r = mockMvc.perform(MockMvcRequestBuilders.get("/api/sessions/export").param("format", "csv"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            String body = r.getResponse().getContentAsString();
            Assertions.assertTrue(body.contains("id,deviceSsid,userName"));
            Assertions.assertTrue(body.contains(saved.getDeviceSsid()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void exportJsonReturnsArray() {
        Session s = new Session();
        s.setDeviceSsid("test-device-02");
        s.setUserName("Tester2");
        s.setVariety("Samba");
        s.setAmyloseClass("Low");
        s.setConfidenceScore(0.85);
        s.setCapturedAt(Instant.now());
        s.setSubmittedAt(Instant.now());
        s.setTrialStage("PYT");
        s.setSeason("Wet");
        s.setImageHash("hash456");
        s.setNeedsReview(false);

        sessionRepository.save(s);

        try {
            MvcResult r = mockMvc.perform(MockMvcRequestBuilders.get("/api/sessions/export").param("format", "json"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            Assertions.assertTrue(r.getResponse().getContentAsString().trim().startsWith("["));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
