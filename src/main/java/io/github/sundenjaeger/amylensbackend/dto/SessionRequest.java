// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.Instant;

public record SessionRequest(
        @NotBlank(message = "Device SSAID is required")
        @Schema(description = "The Android SSAID of the submitting device", example = "abc123xyz")
        String deviceSsaid,

        @NotBlank(message = "Username is required")
        @Schema(description = "Researcher name selected from the authorized user list", example = "Researcher A")
        String userName,

        @NotBlank(message = "Variety is required")
        @Schema(description = "Rice variety name from the server varieties list", example = "IR64")
        String variety,

        @NotBlank(message = "Amylose Class is required")
        @Schema(description = "Classified amylose category", example = "Intermediate", allowableValues = {"Waxy", "Low", "Intermediate", "High"})
        String amyloseClass,

        @NotNull(message = "Confidence score is required")
        @DecimalMin("0.0") @DecimalMax("1.0")
        @Schema(description = "Model confidence score between 0.0 and 1.0", example = "0.91")
        Double confidenceScore,

        @NotNull(message = "Captured At is required")
        @Schema(description = "Timestamp when the image was captured on device. Format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", example = "2026-05-25T09:00:00.000Z")
        Instant capturedAt,

        @NotNull(message = "Submitted At is required")
        @Schema(description = "Timestamp when the session was submitted to the server. Format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", example = "2026-05-25T09:01:00.000Z")
        Instant submittedAt,

        @NotBlank(message = "Trial Stage is required")
        @Schema(description = "Research trial stage", example = "OYT", allowableValues = {"OYT", "PYT", "MET", "NCT"})
        String trialStage,

        @NotBlank(message = "Season is required")
        @Schema(description = "Cropping season", example = "Dry", allowableValues = {"Dry", "Wet"})
        String season,

        @NotBlank(message = "Imagehash is required")
        @Schema(description = "SHA-256 hash of the original grain image for tamper detection", example = "abc123def456...")
        String imageHash
) {
}
