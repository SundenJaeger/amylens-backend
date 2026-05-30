// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.config.CustomUserDetails;
import io.github.sundenjaeger.amylensbackend.dto.*;
import io.github.sundenjaeger.amylensbackend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Login, logout, and password reset for the team lead dashboard. Called by Module 4.")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletRequest servletRequest,
                                               HttpServletResponse servletResponse) {
        try {
            UsernamePasswordAuthenticationToken unauthenticatedToken = UsernamePasswordAuthenticationToken
                    .unauthenticated(request.username(), request.password());

            Authentication authentication = authenticationManager.authenticate(unauthenticatedToken);

            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);

            securityContextHolderStrategy.setContext(context);

            securityContextRepository.saveContext(context, servletRequest, servletResponse);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            return ResponseEntity.ok(new LoginResponse(
                    userDetails.getUser().getId(),
                    userDetails.getUsername(),
                    userDetails.getUser().getRole()
            ));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid name or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

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
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetRequest request,
                                              Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            authService.resetPassword(authentication.getName(), request.currentPassword(), request.newPassword());

            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}