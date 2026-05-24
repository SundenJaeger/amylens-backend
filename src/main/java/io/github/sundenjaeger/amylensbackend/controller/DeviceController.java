package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.dto.DeviceAuthRequest;
import io.github.sundenjaeger.amylensbackend.dto.DeviceAuthResponse;
import io.github.sundenjaeger.amylensbackend.dto.DeviceRegistration;
import io.github.sundenjaeger.amylensbackend.model.Device;
import io.github.sundenjaeger.amylensbackend.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")
public class DeviceController {
    private final DeviceService deviceService;

    @PostMapping("/register")
    public ResponseEntity<Device> register(@Valid @RequestBody DeviceRegistration dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.register(dto));
    }

    @PostMapping("/auth")
    public ResponseEntity<DeviceAuthResponse> checkAuth(@Valid @RequestBody DeviceAuthRequest dto) {
        DeviceAuthResponse response = deviceService.checkAuthStatus(dto.ssaid());
        HttpStatus status = switch (response.status()) {
            case "APPROVED" -> HttpStatus.OK;
            case "PENDING" -> HttpStatus.ACCEPTED;
            default -> HttpStatus.FORBIDDEN;
        };
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/{ssaid}/users")
    public ResponseEntity<List<String>> getUsers(@PathVariable String ssaid) {
        return ResponseEntity.ok(deviceService.getAuthorizedUsers(ssaid));
    }

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        return ResponseEntity.ok(deviceService.findAll());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Device> approve(@PathVariable Long id,
                                          @RequestBody List<String> userNames) {
        return ResponseEntity.ok(deviceService.approve(id, userNames));
    }

    @PutMapping("/{id}/deny")
    public ResponseEntity<Device> deny(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.deny(id));
    }
}
