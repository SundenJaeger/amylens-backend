package io.github.sundenjaeger.amylensbackend.dto;

import io.github.sundenjaeger.amylensbackend.enums.RoleType;

public record RegisterResponse(
        Long id,
        String username,
        RoleType role
) {
}
