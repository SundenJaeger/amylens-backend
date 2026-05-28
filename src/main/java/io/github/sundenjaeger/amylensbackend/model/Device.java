// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.model;

import io.github.sundenjaeger.amylensbackend.enums.DeviceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "devices")
@Entity
@Schema(description = "Represents a registered Android device in the system")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Auto-generated database ID", example = "1")
    private Long id;

    @Column(name = "ssaid", nullable = false, unique = true)
    @Schema(description = "Android SSAID — unique persistent device identifier", example = "test-device-001")
    private String ssaid;

    @Column(name = "device_label", nullable = false)
    @Schema(description = "Human-readable device label set during registration", example = "Field Tablet 01")
    private String deviceLabel;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Current authorization status", example = "APPROVED", allowableValues = {"PENDING", "APPROVED", "DENIED"})

    private DeviceStatus status;

    @ElementCollection
    @CollectionTable(name = "device_user_names", joinColumns = @JoinColumn(name = "device_id"))
    @Column(name = "user_name")
    @Schema(description = "List of researcher names authorized to use this device", example = "[\"Researcher A\", \"Researcher B\"]")
    private List<String> linkedUserNames;

    @Column(name = "registered_at", nullable = false)
    @CreationTimestamp
    @Schema(description = "Timestamp when the device first registered", example = "2026-05-25T07:15:09.151370Z")
    private Instant registeredAt;

    @Column(name = "approved_at")
    @Schema(description = "Timestamp when the device was approved by the team lead. Null if not yet approved.", example = "2026-05-25T08:00:00.000000Z")
    private Instant approvedAt;
}
