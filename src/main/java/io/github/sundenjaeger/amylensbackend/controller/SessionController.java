package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.dto.SessionRequest;
import io.github.sundenjaeger.amylensbackend.model.Session;
import io.github.sundenjaeger.amylensbackend.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class SessionController {
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<Session> ingest(@Valid @RequestBody SessionRequest dto) {
        return ResponseEntity.ok(sessionService.ingest(dto));
    }

    @GetMapping
    public ResponseEntity<List<Session>> getSessions(
            @RequestParam(required = false) String variety,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(sessionService.findAll(variety, status));
    }
}
