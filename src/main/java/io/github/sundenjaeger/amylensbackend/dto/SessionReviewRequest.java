// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SessionReviewRequest(
        @NotBlank(message = "Action is required")
        @Schema(
                description = "Review action to apply to the flagged session",
                example = "ACCEPT",
                allowableValues = {"ACCEPT", "REJECT"}
        )
        String action,

        @NotBlank(message = "Reviewer Identity is required")
        @Schema(
                description = "Identity of the team lead performing the review, recorded as an immutable audit entry",
                example = "admin"
        )
        String reviewerIdentity,

        @Schema(
                description = "Mandatory note required when action is REJECT. Ignored when action is ACCEPT. Request returns 400 if REJECT is submitted without a note.",
                example = "Sample was contaminated — retake required"
        )
        String reviewerNote
) {
}
