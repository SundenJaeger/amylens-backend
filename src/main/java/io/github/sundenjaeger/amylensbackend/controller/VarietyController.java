package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.dto.VarietyRequest;
import io.github.sundenjaeger.amylensbackend.dto.VarietyResponse;
import io.github.sundenjaeger.amylensbackend.model.Variety;
import io.github.sundenjaeger.amylensbackend.service.VarietyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/varieties")
@Tag(name = "Varieties", description = "Endpoint for retrieving the registered rice variety list. Called by Module 2 on startup to populate the variety dropdown.")
public class VarietyController {
    private final VarietyService varietyService;

    @PostMapping
    @Operation(
            summary = "Add a new variety",
            description = "Adds a new rice variety to the registered list. Called by Module 4 admin area. Requires login session."
    )
    @ApiResponse(responseCode = "201", description = "Variety created successfully")
    public ResponseEntity<VarietyResponse> create(@RequestBody @Valid VarietyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(varietyService.create(request));
    }

    @GetMapping
    @Operation(
            summary = "Get all registered varieties",
            description = "Called by Module 2 on device startup. No authentication required. " +
                    "Returns the full list of registered rice varieties for display in the session metadata dropdown. " +
                    "Returns an empty array if no varieties have been seeded yet."
    )
    @ApiResponse(responseCode = "200", description = "List of registered varieties")
    public ResponseEntity<List<VarietyResponse>> getAll() {
        return ResponseEntity.ok(varietyService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a variety",
            description = "Updates an existing rice variety in the registered list. " +
                    "Called by Module 4 admin area. Requires login session."
    )
    @ApiResponse(responseCode = "200", description = "Variety updated successfully")
    @ApiResponse(responseCode = "404", description = "Variety not found")
    public ResponseEntity<VarietyResponse> update(@PathVariable Long id,
                                                  @RequestBody @Valid VarietyRequest request) {
        return ResponseEntity.ok(varietyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a variety",
            description = "Removes a variety from the registered list. Called by Module 4 admin area. Requires login session."
    )
    @ApiResponse(responseCode = "204", description = "Variety deleted successfully")
    @ApiResponse(responseCode = "404", description = "Variety not found")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        varietyService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
