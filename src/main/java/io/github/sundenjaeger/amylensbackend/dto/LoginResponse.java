package io.github.sundenjaeger.amylensbackend.dto;

import io.github.sundenjaeger.amylensbackend.enums.RoleType;

public record LoginResponse(
        Long id,
        String username,
        RoleType role
) {
}
