package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.dto.GqrisImportResult;
import io.github.sundenjaeger.amylensbackend.service.GqrisImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/gqris")
public class GqrisImportController {
    private final GqrisImportService gqrisImportService;

    @PostMapping("/import")
    public ResponseEntity<GqrisImportResult> importCsv(
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(gqrisImportService.importFromCsv(file));
    }
}
