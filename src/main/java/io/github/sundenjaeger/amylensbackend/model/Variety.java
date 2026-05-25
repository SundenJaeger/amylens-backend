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
@Table(name = "varieties")
@Entity
@Schema(description = "Represents a registered rice variety available for session metadata selection")
public class Variety {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Auto-generated database ID", example = "1")
    private Long id;

    @Column(name = "name", nullable = false)
    @Schema(description = "Rice variety name", example = "IR64")
    private String name;

    @Column(name = "description", nullable = false)
    @Schema(description = "Optional description of the variety", example = "Popular indica variety")
    private String description;
}
