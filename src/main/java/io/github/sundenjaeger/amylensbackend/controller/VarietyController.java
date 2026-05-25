package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.model.Variety;
import io.github.sundenjaeger.amylensbackend.service.VarietyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/varieties")
@Tag(name = "Varieties", description = "Endpoint for retrieving the registered rice variety list. Called by Module 2 on startup to populate the variety dropdown.")
public class VarietyController {
    private final VarietyService varietyService;

    @GetMapping
    @Operation(
            summary = "Get all registered varieties",
            description = "Called by Module 2 on device startup. No authentication required. " +
                    "Returns the full list of registered rice varieties for display in the session metadata dropdown. " +
                    "Returns an empty array if no varieties have been seeded yet."
    )
    @ApiResponse(responseCode = "200", description = "List of registered varieties")
    public ResponseEntity<List<Variety>> getAll() {
        return ResponseEntity.ok(varietyService.findAll());
    }
}
