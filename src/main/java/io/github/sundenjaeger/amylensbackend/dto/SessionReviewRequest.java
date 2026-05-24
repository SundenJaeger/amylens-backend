// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record SessionReviewRequest(
        @NotBlank(message = "Action is required")
        String action,

        @NotBlank(message = "Reviewer Identity is required")
        String reviewerIdentity,

        String reviewerNote
) {
}
