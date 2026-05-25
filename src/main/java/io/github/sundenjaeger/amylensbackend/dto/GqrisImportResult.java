// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Result returned after a GQ-RIS CSV import operation")
public record GqrisImportResult(

        @Schema(
                description = "Number of rows successfully parsed and inserted into the gqris_mirror table",
                example = "42"
        )
        Integer rowsImported,

        @Schema(
                description = "Number of rows skipped due to validation errors",
                example = "2"
        )
        Integer rowsSkipped,

        @Schema(
                description = "List of error messages for skipped rows, each prefixed with the row number",
                example = "[\"Row 3: Not enough columns\", \"Row 7: Invalid integer format\"]"
        )
        List<String> errors
) {
}
