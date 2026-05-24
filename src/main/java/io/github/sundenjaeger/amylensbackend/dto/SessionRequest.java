// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record SessionRequest(
        @NotBlank(message = "Device SSAID is required")
        String deviceSsaid,

        @NotBlank(message = "Username is required")
        String userName,

        @NotBlank(message = "Variety is required")
        String variety,

        @NotBlank(message = "Amylose Class is required")
        String amyloseClass,

        @NotNull(message = "Confidence score is required")
        Integer confidenceScore,

        @NotNull(message = "Captured At is required")
        Instant capturedAt,

        @NotNull(message = "Submitted At is required")
        Instant submittedAt,

        @NotBlank(message = "Trial Stage is required")
        String trialStage,

        @NotBlank(message = "Season is required")
        String season,

        @NotBlank(message = "Imagehash is required")
        String imageHash
) {
}
