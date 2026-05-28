package io.github.sundenjaeger.amylensbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record VarietyRequest(
        @NotBlank(message = "Name is required")
        String name,

        String description
) {
}
