package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.dto.GqrisImportResult;
import io.github.sundenjaeger.amylensbackend.service.GqrisImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "GQ-RIS Mirror Management", description = "Endpoint for importing the GQ-RIS historical data mirror. Called by Module 4 admin area. Requires login session.")
public class GqrisImportController {
    private final GqrisImportService gqrisImportService;

    @PostMapping("/import")
    @Operation(
            summary = "Import GQ-RIS historical data from CSV",
            description = "Called by Module 4 admin upload button. Requires login session. " +
                    "Accepts a CSV file with three columns: variety (String), amyloseOrdinal (Integer: Waxy=1 Low=2 Intermediate=3 High=4), year (Integer). " +
                    "First row is treated as a header and skipped. " +
                    "Performs a full replace — existing records are deleted before new ones are inserted. " +
                    "Invalid rows are skipped and reported in the errors field of the response."
    )
    @ApiResponse(responseCode = "200", description = "Import completed — check rowsImported, rowsSkipped, and errors in response")
    @ApiResponse(responseCode = "400", description = "No valid rows found in the uploaded file")
    @ApiResponse(responseCode = "403", description = "Not authenticated")
    public ResponseEntity<GqrisImportResult> importCsv(
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(gqrisImportService.importFromCsv(file));
    }
}
