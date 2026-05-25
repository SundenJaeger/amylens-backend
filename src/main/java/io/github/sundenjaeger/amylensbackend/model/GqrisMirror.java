// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "gqris_mirrors")
@Entity
@Schema(description = "Represents one historical amylose record from the GQ-RIS mirror, used for anomaly detection comparison")
public class GqrisMirror {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Auto-generated database ID", example = "1")
    private Long id;

    @Column(name = "variety", nullable = false)
    @Schema(description = "Rice variety this record belongs to", example = "IR64")
    private String variety;

    @Column(name = "amylose_ordinal", nullable = false)
    @Schema(description = "Numeric amylose ordinal for statistical comparison. Waxy=1, Low=2, Intermediate=3, High=4", example = "3")
    private Integer amyloseOrdinal;

    @Column(name = "year", nullable = false)
    @Schema(description = "Year this historical record was collected", example = "2023")
    private Integer year;
}
