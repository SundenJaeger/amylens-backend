// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DeviceRegistration(
        @NotBlank(message = "SSAID is required")
        @Schema(
                description = "The Android SSAID of the device being registered",
                example = "test-device-001"
        )
        String ssaid,

        @NotBlank(message = "Device Label is required")
        @Schema(
                description = "A human-readable label for the device, used by the team lead to identify it on the dashboard",
                example = "Field Tablet 01"
        )
        String deviceLabel
) {
}
