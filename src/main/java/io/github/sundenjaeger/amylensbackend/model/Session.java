// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Represents a submitted classification session record stored on the server")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Auto-generated database ID", example = "1")
    private Long id;

    @Column(name = "device_ssid", nullable = false)
    @Schema(description = "SSAID of the device that submitted this session", example = "test-device-001")
    private String deviceSsid;

    @Column(name = "user_name", nullable = false)
    @Schema(description = "Researcher name recorded for this session", example = "Researcher A")
    private String userName;

    @Column(name = "variety", nullable = false)
    @Schema(description = "Rice variety classified in this session", example = "IR64")
    private String variety;

    @Column(name = "amylose_class", nullable = false)
    @Schema(description = "Amylose class returned by the on-device classifier", example = "Intermediate", allowableValues = {"Waxy", "Low", "Intermediate", "High"})
    private String amyloseClass;

    @Column(name = "confidence_score", nullable = false)
    @Schema(description = "Model confidence score between 0.0 and 1.0", example = "0.91")
    private Double confidenceScore;

    @Column(name = "captured_at", nullable = false)
    @Schema(description = "Timestamp when the image was captured on the device", example = "2026-05-25T09:00:00.000Z")
    private Instant capturedAt;

    @Column(name = "submitted_at", nullable = false)
    @Schema(description = "Timestamp when the session was submitted to the server", example = "2026-05-25T09:01:00.000Z")
    private Instant submittedAt;

    @Column(name = "verdict")
    @Schema(description = "Anomaly detection verdict assigned after processing. Null while still PENDING.", example = "needs_review", allowableValues = {"verified", "needs_review", "ACCEPTED", "REJECTED"})
    private String verdict;

    @Column(name = "verdict_reason")
    @Schema(description = "Sub-reason for needs_review verdict. Null if verdict is verified.", example = "insufficient_data", allowableValues = {"insufficient_data", "low_confidence", "deviation"})
    private String verdictReason;

    @Column(name = "needs_review", nullable = false)
    @Schema(description = "True if this session requires team lead review", example = "true")
    private Boolean needsReview;

    @Column(name = "reviewer_identity")
    @Schema(description = "Identity of the team lead who reviewed this session. Null if not yet reviewed.", example = "admin")
    private String reviewerIdentity;

    @Column(name = "reviewer_note")
    @Schema(description = "Note recorded by the team lead on rejection. Null if not rejected.", example = "Sample was contaminated")
    private String reviewerNote;

    @Column(name = "reviewer_timestamp")
    @Schema(description = "Timestamp when the review was recorded on the server. Null if not yet reviewed.", example = "2026-05-25T09:05:00.000Z")
    private Instant reviewerTimestamp;

    @Column(name = "trial_stage", nullable = false)
    @Schema(description = "Trial stage of this session", example = "OYT", allowableValues = {"OYT", "PYT", "MET", "NCT"})
    private String trialStage;

    @Column(name = "season", nullable = false)
    @Schema(description = "Cropping season of this session", example = "Dry", allowableValues = {"Dry", "Wet"})
    private String season;

    @Column(name = "image_hash", nullable = false)
    @Schema(description = "SHA-256 hash of the original grain image for tamper detection", example = "abc123def456...")
    private String imageHash;
}
