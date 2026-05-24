// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "sessions")
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_ssid", nullable = false)
    private String deviceSsid;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "variety", nullable = false)
    private String variety;

    @Column(name = "amylose_class", nullable = false)
    private String amyloseClass;

    @Column(name = "confidence_score", nullable = false)
    private Integer confidenceScore;

    @Column(name = "captured_at", nullable = false)
    private Instant capturedAt;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Column(name = "verdict")
    private String verdict;

    @Column(name = "verdict_reason")
    private String verdictReason;

    @Column(name = "needs_review", nullable = false)
    private Boolean needsReview;

    @Column(name = "reviewer_identity")
    private String reviewerIdentity;

    @Column(name = "reviewer_note")
    private String reviewerNote;

    @Column(name = "trial_stage", nullable = false)
    private String trialStage;

    @Column(name = "season", nullable = false)
    private String season;

    @Column(name = "image_hash", nullable = false)
    private String imageHash;
}
