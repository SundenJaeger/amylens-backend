package io.github.sundenjaeger.amylensbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetRequest(
        @NotBlank(message = "Current password is required")
        @Schema(description = "The team lead's current password", example = "oldPassword123!")
        String currentPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must be at least 8 characters")
        @Schema(description = "The new password. Minimum 8 characters.", example = "newPassword456!")
        String newPassword
) {
}