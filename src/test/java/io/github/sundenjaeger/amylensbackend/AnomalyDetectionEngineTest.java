// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend;

import io.github.sundenjaeger.amylensbackend.model.GqrisMirror;
import io.github.sundenjaeger.amylensbackend.model.Session;
import io.github.sundenjaeger.amylensbackend.repository.GqrisMirrorRepository;
import io.github.sundenjaeger.amylensbackend.repository.SessionRepository;
import io.github.sundenjaeger.amylensbackend.service.AnomalyDetectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AnomalyDetectionEngineTest {

    private AnomalyDetectionService anomalyDetectionService;
    private GqrisMirrorRepository gqrisRepo;
    private SessionRepository sessionRepository;

    @BeforeEach
    void setup() {
        gqrisRepo = mock(GqrisMirrorRepository.class);
        sessionRepository = mock(SessionRepository.class);
        anomalyDetectionService = new AnomalyDetectionService(gqrisRepo, sessionRepository);
    }

    @Test
    void fewerThan3HistoricalRecords_shouldFlagInsufficient() {
        Session session = new Session();
        session.setVariety("IR64");
        session.setAmyloseClass("Intermediate");
        session.setConfidenceScore(0.91);

        when(gqrisRepo.findByVariety("IR64"))
                .thenReturn(List.of());

        when(sessionRepository.save(any())).thenReturn(session);

        anomalyDetectionService.processAsync(session);

        verify(sessionRepository).save(argThat(s ->
                s.getVerdict().equals("needs_review") &&
                        s.getVerdictReason().equals("insufficient_data")
        ));
    }

    @Test
    void confidenceBelowThreshold_shouldFlagLowConfidence() {
        Session session = new Session();
        session.setVariety("IR64");
        session.setAmyloseClass("Intermediate");
        session.setConfidenceScore(0.80);

        List<GqrisMirror> history = List.of(
                mirrorRecord("IR64", 3, 2023),
                mirrorRecord("IR64", 3, 2022),
                mirrorRecord("IR64", 3, 2021)
        );

        when(gqrisRepo.findByVariety("IR64")).thenReturn(history);
        when(sessionRepository.save(any())).thenReturn(session);

        anomalyDetectionService.processAsync(session);

        verify(sessionRepository).save(argThat(s ->
                s.getVerdict().equals("needs_review") &&
                        s.getVerdictReason().equals("low_confidence")
        ));
    }

    @Test
    void resultWithinNormalRange_shouldVerify() {
        Session session = new Session();
        session.setVariety("IR64");
        session.setAmyloseClass("Intermediate");
        session.setConfidenceScore(0.91);

        List<GqrisMirror> history = List.of(
                mirrorRecord("IR64", 3, 2023),
                mirrorRecord("IR64", 3, 2022),
                mirrorRecord("IR64", 3, 2021)
        );

        when(gqrisRepo.findByVariety("IR64")).thenReturn(history);
        when(sessionRepository.save(any())).thenReturn(session);

        anomalyDetectionService.processAsync(session);

        verify(sessionRepository).save(argThat(s ->
                s.getVerdict().equals("verified")
        ));
    }

    @Test
    void resultOutsideNormalRange_shouldFlagDeviation() {
        Session session = new Session();
        session.setVariety("IR64");
        session.setAmyloseClass("Waxy");
        session.setConfidenceScore(0.91);

        List<GqrisMirror> history = List.of(
                mirrorRecord("IR64", 4, 2023),
                mirrorRecord("IR64", 4, 2022),
                mirrorRecord("IR64", 4, 2021)
        );

        when(gqrisRepo.findByVariety("IR64")).thenReturn(history);
        when(sessionRepository.save(any())).thenReturn(session);

        anomalyDetectionService.processAsync(session);

        verify(sessionRepository).save(argThat(s ->
                s.getVerdict().equals("needs_review") &&
                        s.getVerdictReason().equals("deviation")
        ));
    }

    private GqrisMirror mirrorRecord(String variety, int ordinal, int year) {
        GqrisMirror g = new GqrisMirror();
        g.setVariety(variety);
        g.setAmyloseOrdinal(ordinal);
        g.setYear(year);
        return g;
    }
}