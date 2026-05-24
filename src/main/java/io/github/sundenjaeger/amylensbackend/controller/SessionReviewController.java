package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.dto.SessionReviewRequest;
import io.github.sundenjaeger.amylensbackend.model.Session;
import io.github.sundenjaeger.amylensbackend.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class SessionReviewController {
    private final SessionService sessionService;

    @PostMapping("/{id}/review")
    public ResponseEntity<Session> review(@PathVariable Long id,
                                          @Valid @RequestBody SessionReviewRequest dto) {
        return ResponseEntity.ok(sessionService.review(id, dto));
    }
}
