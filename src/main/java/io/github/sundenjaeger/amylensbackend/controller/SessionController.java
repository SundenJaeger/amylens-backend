package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.dto.SessionRequest;
import io.github.sundenjaeger.amylensbackend.model.Session;
import io.github.sundenjaeger.amylensbackend.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
@Tag(name = "Session Management", description = "Endpoints for session submission and retrieval. POST is called by Module 2. GET is called by Module 4.")
public class SessionController {
    private final SessionService sessionService;

    @PostMapping
    @Operation(
            summary = "Submit a session record",
            description = "Called by Module 2 after on-device classification. Stores the session and triggers anomaly detection asynchronously. Device must be approved or request is rejected with 403."
    )
    @ApiResponse(responseCode = "200", description = "Session stored successfully and queued for anomaly detection")
    @ApiResponse(responseCode = "403", description = "Device SSAID is not registered or not approved")
    @ApiResponse(responseCode = "400", description = "Validation failed — check confidenceScore range (0.0-1.0) and timestamp format (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')")
    public ResponseEntity<Session> ingest(@Valid @RequestBody SessionRequest dto) {
        return ResponseEntity.ok(sessionService.ingest(dto));
    }

    @GetMapping
    @Operation(
            summary = "Get all sessions",
            description = "Called by Module 4 dashboard. Requires login session. Supports optional filtering by variety and verdict status."
    )
    @ApiResponse(responseCode = "200", description = "List of session records with verdicts")
    @ApiResponse(responseCode = "403", description = "Not authenticated")
    public ResponseEntity<List<Session>> getSessions(
            @RequestParam(required = false)
            @Parameter(description = "Filter by rice variety name", example = "IR64")
            String variety,

            @RequestParam(required = false)
            @Parameter(description = "Filter by verdict status", example = "needs_review", schema = @Schema(allowableValues = {"verified", "needs_review", "ACCEPTED", "REJECTED"}))
            String status) {
        return ResponseEntity.ok(sessionService.findAll(variety, status));
    }
}
