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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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

    @GetMapping("/export")
    @Operation(summary = "Export sessions as CSV or JSON", description = "Streams exported sessions. Query params: format=csv|json, variety, status, dateFrom (ISO), dateTo (ISO)")
    public ResponseEntity<?> exportSessions(@RequestParam(defaultValue = "csv") String format,
                                            @RequestParam(required = false) String variety,
                                            @RequestParam(required = false) String status,
                                            @RequestParam(required = false) String dateFrom,
                                            @RequestParam(required = false) String dateTo) {
        Instant from = null, to = null;
        try {
            if (dateFrom != null) from = Instant.parse(dateFrom);
            if (dateTo != null) to = Instant.parse(dateTo);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format; use ISO-8601 UTC timestamps.");
        }

        List<Session> rows = sessionService.export(variety, status, from, to);

        if ("json".equalsIgnoreCase(format)) {
            return ResponseEntity.ok(rows);
        }

        // Default to CSV
        StringBuilder sb = new StringBuilder();
        // Header (14 columns)
        sb.append("id,deviceSsid,userName,variety,amyloseClass,confidenceScore,capturedAt,submittedAt,verdict,verdictReason,trialStage,season,imageHash,reviewerIdentity\n");
        for (Session s : rows) {
            sb.append(s.getId()).append(',')
                    .append(escape(s.getDeviceSsid())).append(',')
                    .append(escape(s.getUserName())).append(',')
                    .append(escape(s.getVariety())).append(',')
                    .append(escape(s.getAmyloseClass())).append(',')
                    .append(s.getConfidenceScore() != null ? s.getConfidenceScore() : "").append(',')
                    .append(s.getCapturedAt() != null ? s.getCapturedAt().toString() : "").append(',')
                    .append(s.getSubmittedAt() != null ? s.getSubmittedAt().toString() : "").append(',')
                    .append(escape(s.getVerdict())).append(',')
                    .append(escape(s.getVerdictReason())).append(',')
                    .append(escape(s.getTrialStage())).append(',')
                    .append(escape(s.getSeason())).append(',')
                    .append(escape(s.getImageHash())).append(',')
                    .append(escape(s.getReviewerIdentity())).append('\n');
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        InputStreamResource resource = new InputStreamResource(inputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=amylens-export.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(resource);
    }

    private static String escape(String v) {
        if (v == null) return "";
        String s = v.replace("\"", "\"\"");
        if (s.contains(",") || s.contains("\n") || s.contains("\r")) {
            return '"' + s + '"';
        }
        return s;
    }
}
