package io.github.sundenjaeger.amylensbackend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
@Tag(name = "Export", description = "Export endpoints for CSV, JSON, and PDF session data conforming to the 14-column GQ-RIS schema. Requires login session. Under construction — available after core MVP.")
public class ExportController {
}
