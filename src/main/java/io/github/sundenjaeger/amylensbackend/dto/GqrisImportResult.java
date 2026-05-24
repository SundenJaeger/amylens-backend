// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.dto;

import java.util.List;

public record GqrisImportResult(
        Integer rowsImported,
        Integer rowsSkipped,
        List<String> errors
) {
}
