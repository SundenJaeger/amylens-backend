// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DeviceAuthRequest(
        @NotBlank(message = "SSAID is required")
        @Schema(
                description = "The Android SSAID of the device requesting authorization check",
                example = "test-device-001"
        )
        String ssaid
) {
}
