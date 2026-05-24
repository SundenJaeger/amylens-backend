// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.model;

import io.github.sundenjaeger.amylensbackend.enums.DeviceStatus;
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
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ssaid", nullable = false)
    private String ssaid;

    @Column(name = "device_label", nullable = false)
    private String deviceLabel;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceStatus status;

    @ElementCollection
    @CollectionTable(name = "device_user_names", joinColumns = @JoinColumn(name = "device_id"))
    @Column(name = "user_name")
    private List<String> linkedUserNames;

    @Column(name = "registered_at", nullable = false)
    @CreationTimestamp
    private Instant registeredAt;

    @Column(name = "approved_at")
    private Instant approvedAt;
}
