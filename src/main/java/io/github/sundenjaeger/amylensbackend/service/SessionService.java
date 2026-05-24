// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.service;

import io.github.sundenjaeger.amylensbackend.dto.SessionRequest;
import io.github.sundenjaeger.amylensbackend.dto.SessionReviewRequest;
import io.github.sundenjaeger.amylensbackend.enums.DeviceStatus;
import io.github.sundenjaeger.amylensbackend.exception.MissingReviewNoteException;
import io.github.sundenjaeger.amylensbackend.exception.SessionNotFoundException;
import io.github.sundenjaeger.amylensbackend.exception.UnauthorizedDeviceException;
import io.github.sundenjaeger.amylensbackend.model.Device;
import io.github.sundenjaeger.amylensbackend.model.Session;
import io.github.sundenjaeger.amylensbackend.repository.DeviceRepository;
import io.github.sundenjaeger.amylensbackend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final DeviceRepository deviceRepository;
    private final AnomalyDetectionService anomalyDetectionService;

    public Session ingest(SessionRequest dto) {
        Device device = deviceRepository.findBySsaid(dto.deviceSsaid())
                .orElseThrow(() -> new UnauthorizedDeviceException("Device not registered: " + dto.deviceSsaid()));

        if (device.getStatus() != DeviceStatus.APPROVED) {
            throw new UnauthorizedDeviceException("Device not approved: " + dto.deviceSsaid());
        }

        Session session = new Session();
        session.setDeviceSsid(dto.deviceSsaid());
        session.setUserName(dto.userName());
        session.setVariety(dto.variety());
        session.setAmyloseClass(dto.amyloseClass());
        session.setConfidenceScore(dto.confidenceScore());
        session.setCapturedAt(dto.capturedAt());
        session.setSubmittedAt(dto.submittedAt());
        session.setTrialStage(dto.trialStage());
        session.setSeason(dto.season());
        session.setImageHash(dto.imageHash());
        session.setNeedsReview(false);

        Session saved = sessionRepository.save(session);

        anomalyDetectionService.processAsync(saved);

        return saved;
    }

    public List<Session> findAll(String variety, String status) {
        if (variety != null && status != null) {
            return sessionRepository.findByVariety(variety)
                    .stream()
                    .filter(s -> s.getVerdict() != null && s.getVerdict().equalsIgnoreCase(status))
                    .toList();
        }

        if (variety != null) {
            return sessionRepository.findByVariety(variety);
        }

        if (status != null) {
            return sessionRepository.findByVerdict(status);
        }
        return sessionRepository.findAll();
    }

    public Session review(Long id, SessionReviewRequest dto) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException("Session not found: " + id));

        if (dto.action().equalsIgnoreCase("REJECT")) {
            if (dto.reviewerNote() == null || dto.reviewerNote().isBlank()) {
                throw new MissingReviewNoteException("A note is required when rejecting a session");
            }
            session.setReviewerNote(dto.reviewerNote());
        }

        session.setVerdict(dto.action().toUpperCase());
        session.setReviewerIdentity(dto.reviewerIdentity());
        session.setNeedsReview(false);

        return sessionRepository.save(session);
    }
}
