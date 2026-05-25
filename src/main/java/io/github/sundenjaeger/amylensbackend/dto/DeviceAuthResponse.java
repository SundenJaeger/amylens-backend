// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned by the device authorization check endpoint")
public record DeviceAuthResponse(
        @Schema(
                description = "Current authorization status of the device",
                example = "APPROVED",
                allowableValues = {"APPROVED", "PENDING", "DENIED"}
        )
        String status
) {
}
