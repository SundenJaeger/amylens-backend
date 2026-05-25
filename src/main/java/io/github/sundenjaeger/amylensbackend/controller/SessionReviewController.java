package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.dto.SessionReviewRequest;
import io.github.sundenjaeger.amylensbackend.model.Session;
import io.github.sundenjaeger.amylensbackend.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
@Tag(name = "Session Review", description = "Endpoint for team lead review actions on flagged sessions. Called by Module 4. Requires login session.")
public class SessionReviewController {
    private final SessionService sessionService;

    @PostMapping("/{id}/review")
    @Operation(
            summary = "Accept or reject a flagged session",
            description = "Called by Module 4 dashboard. Requires login session. " +
                    "Action must be ACCEPT or REJECT. " +
                    "reviewerNote is mandatory when action is REJECT — request will be rejected with 400 if note is missing or blank. " +
                    "reviewerNote is ignored when action is ACCEPT."
    )
    @ApiResponse(responseCode = "200", description = "Session review recorded successfully")
    @ApiResponse(responseCode = "400", description = "REJECT action submitted without a mandatory reviewer note")
    @ApiResponse(responseCode = "403", description = "Not authenticated")
    @ApiResponse(responseCode = "404", description = "Session not found for the given ID")
    public ResponseEntity<Session> review(@PathVariable Long id,
                                          @Valid @RequestBody SessionReviewRequest dto) {
        return ResponseEntity.ok(sessionService.review(id, dto));
    }
}
