// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.service;

import io.github.sundenjaeger.amylensbackend.model.GqrisMirror;
import io.github.sundenjaeger.amylensbackend.model.Session;
import io.github.sundenjaeger.amylensbackend.repository.GqrisMirrorRepository;
import io.github.sundenjaeger.amylensbackend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnomalyDetectionService {
    private final GqrisMirrorRepository gqrisRepo;
    private final SessionRepository sessionRepository;

    @Async
    public void processAsync(Session session) {
        List<GqrisMirror> history = gqrisRepo.findByVariety(session.getVariety());

        String verdict;
        String verdictReason;

        if (session.getConfidenceScore() < 0.85) {
            verdict = "needs_review";
            verdictReason = "low_confidence";
        } else if (history.size() < 3) {
            verdict = "needs_review";
            verdictReason = "insufficient_data";
        } else {
            int ordinal = toOrdinal(session.getAmyloseClass());
            double mean = history.stream()
                    .mapToInt(GqrisMirror::getAmyloseOrdinal)
                    .average()
                    .orElse(0);
            double stdDev = Math.sqrt(history.stream()
                    .mapToDouble(g -> Math.pow(g.getAmyloseOrdinal() - mean, 2))
                    .average()
                    .orElse(0));

            if (ordinal < mean - 2 * stdDev || ordinal > mean + 2 * stdDev) {
                verdict = "needs_review";
                verdictReason = "deviation";
            } else {
                verdict = "verified";
                verdictReason = null;
            }
        }

        session.setVerdict(verdict);
        session.setVerdictReason(verdictReason);
        session.setNeedsReview(verdict.equals("needs_review"));
        sessionRepository.save(session);
    }

    private int toOrdinal(String amyloseClass) {
        return switch (amyloseClass.toLowerCase()) {
            case "waxy" -> 1;
            case "low" -> 2;
            case "intermediate" -> 3;
            case "high" -> 4;
            default -> throw new IllegalArgumentException("Unknown amylose class: " + amyloseClass);
        };
    }
}
