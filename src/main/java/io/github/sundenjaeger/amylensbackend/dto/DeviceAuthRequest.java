// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record DeviceAuthRequest(
        @NotBlank(message = "SSAID is required")
        String ssaid
) {
}
