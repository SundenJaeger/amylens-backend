// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "gqris_mirrors")
@Entity
public class GqrisMirror {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "variety", nullable = false)
    private String variety;

    @Column(name = "amylose_ordinal", nullable = false)
    private Integer amyloseOrdinal;

    @Column(name = "year", nullable = false)
    private Integer year;
}
