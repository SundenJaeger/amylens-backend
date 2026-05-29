package io.github.sundenjaeger.amylensbackend.controller;

import io.github.sundenjaeger.amylensbackend.dto.DeviceAuthRequest;
import io.github.sundenjaeger.amylensbackend.dto.DeviceAuthResponse;
import io.github.sundenjaeger.amylensbackend.dto.DeviceRegistration;
import io.github.sundenjaeger.amylensbackend.model.Device;
import io.github.sundenjaeger.amylensbackend.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")
@Tag(name = "Device Management", description = "Endpoints for device registration, authorization, and approval. Called by Module 1 and Module 4.")
public class DeviceController {
    private final DeviceService deviceService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a device",
            description = "Called by Module 1 on first setup. Creates a PENDING device record. Safe to call multiple times — returns existing record if already registered."
    )
    @ApiResponse(responseCode = "201", description = "Device registered successfully")
    public ResponseEntity<Device> register(@Valid @RequestBody DeviceRegistration dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.register(dto));
    }

    @PostMapping("/auth")
    @Operation(
            summary = "Check device authorization status",
            description = "Called by Module 1 on every app launch. Returns APPROVED (200), PENDING (202), or DENIED (403)."
    )
    @ApiResponse(responseCode = "200", description = "Device is approved")
    @ApiResponse(responseCode = "202", description = "Device is pending approval")
    @ApiResponse(responseCode = "403", description = "Device is denied or not registered")
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
    @Operation(
            summary = "Get authorized user list for a device",
            description = "Called by Module 2 on startup. Returns the list of researcher names linked to the given device SSAID."
    )
    @ApiResponse(responseCode = "200", description = "List of authorized user names")
    @ApiResponse(responseCode = "403", description = "Device not approved")
    public ResponseEntity<List<String>> getUsers(@PathVariable String ssaid) {
        return ResponseEntity.ok(deviceService.getAuthorizedUsers(ssaid));
    }

    @GetMapping
    @Operation(
            summary = "Get all devices",
            description = "Called by Module 4 dashboard. Requires login session. Returns all registered devices with their status."
    )
    @ApiResponse(responseCode = "200", description = "List of all devices")
    @ApiResponse(responseCode = "403", description = "Not authenticated")
    public ResponseEntity<List<Device>> getAllDevices() {
        return ResponseEntity.ok(deviceService.findAll());
    }

    @PutMapping("/{id}/approve")
    @Operation(
            summary = "Approve a device",
            description = "Called by Module 4 dashboard. Requires login session. Links the device to a list of researcher names and sets status to APPROVED."
    )
    @ApiResponse(responseCode = "200", description = "Device approved")
    public ResponseEntity<Device> approve(@PathVariable Long id,
                                          @RequestBody List<String> userNames) {
        return ResponseEntity.ok(deviceService.approve(id, userNames));
    }

    @PutMapping("/{id}/deny")
    @Operation(
            summary = "Deny a device",
            description = "Called by Module 4 dashboard. Requires login session. Sets device status to DENIED."
    )
    @ApiResponse(responseCode = "200", description = "Device denied")
    public ResponseEntity<Device> deny(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.deny(id));
    }

    @GetMapping("/researchers")
    @Operation(
            summary = "Get all researchers",
            description = "Returns a deduplicated, sorted list of all researcher names linked to approved devices. " +
                    "No SSAID required. Called by Module 4 dashboard for displaying researcher lists."
    )
    @ApiResponse(responseCode = "200", description = "List of all researcher names across all approved devices")
    public ResponseEntity<List<String>> getAllResearchers() {
        return ResponseEntity.ok(deviceService.getAllResearchers());
    }
}
