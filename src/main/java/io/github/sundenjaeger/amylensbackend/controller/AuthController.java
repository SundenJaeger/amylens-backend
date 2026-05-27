// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.dto.PasswordResetRequest;
import io.github.sundenjaeger.amylensbackend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Login, logout, and password reset for the team lead dashboard. Called by Module 4.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reset")
    @Operation(
            summary = "Reset the authenticated team lead's password",
            description = "Called by Module 4 after first login (Tx 3.3b). " +
                    "Requires an active login session (HttpOnly cookie). " +
                    "Verifies the current password before applying the new one. " +
                    "New password must be at least 8 characters."
    )
    @ApiResponse(responseCode = "200", description = "Password updated successfully")
    @ApiResponse(responseCode = "400", description = "New password fails validation (min 8 chars)")
    @ApiResponse(responseCode = "401", description = "Current password is incorrect or no active session")
    @ApiResponse(responseCode = "403", description = "Not authenticated")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody PasswordResetRequest request,
            Authentication authentication) {

        try {
            authService.resetPassword(authentication.getName(), request.currentPassword(), request.newPassword());
            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
