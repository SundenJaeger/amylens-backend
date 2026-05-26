// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.service;

import io.github.sundenjaeger.amylensbackend.dto.DeviceAuthResponse;
import io.github.sundenjaeger.amylensbackend.dto.DeviceRegistration;
import io.github.sundenjaeger.amylensbackend.enums.DeviceStatus;
import io.github.sundenjaeger.amylensbackend.exception.UnauthorizedDeviceException;
import io.github.sundenjaeger.amylensbackend.model.Device;
import io.github.sundenjaeger.amylensbackend.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public Device register(DeviceRegistration registration) {
        return deviceRepository.findBySsaid(registration.ssaid())
                .orElseGet(() -> {
                    Device device = new Device();
                    device.setSsaid(registration.ssaid());
                    device.setDeviceLabel(registration.deviceLabel());
                    device.setStatus(DeviceStatus.PENDING);
                    device.setLinkedUserNames(new ArrayList<>());

                    return deviceRepository.save(device);
                });
    }

    public DeviceAuthResponse checkAuthStatus(String ssaid) {
        Device device = deviceRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new UnauthorizedDeviceException("Device not registered: " + ssaid));

        return new DeviceAuthResponse(device.getStatus().name());
    }

    public List<String> getAuthorizedUsers(String ssaid) {
        Device device = deviceRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new UnauthorizedDeviceException("Device not found: " + ssaid));

        if (device.getStatus() != DeviceStatus.APPROVED) {
            throw new UnauthorizedDeviceException("Device not approved: " + ssaid);
        }

        return device.getLinkedUserNames();
    }

    @Cacheable("allDevices")
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    @CacheEvict(value = {"devices", "allDevices"}, allEntries = true)
    public Device approve(Long id, List<String> usernames) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedDeviceException("Device not found: " + id));
        device.setStatus(DeviceStatus.APPROVED);
        device.setLinkedUserNames(usernames);
        device.setApprovedAt(Instant.now());

        return deviceRepository.save(device);
    }

    @CacheEvict(value = {"devices", "allDevices"}, allEntries = true)
    public Device deny(Long id) {
        Device device = deviceRepository.findById(id).
                orElseThrow(() -> new UnauthorizedDeviceException("Device not found: " + id));
        device.setStatus(DeviceStatus.DENIED);

        return deviceRepository.save(device);
    }
}
