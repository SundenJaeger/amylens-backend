package io.github.sundenjaeger.amylensbackend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Dashboard Analytics", description = "Analytics endpoints for Module 4 dashboard charts. Requires login session. Under construction — available after core MVP.")
public class DashboardController {
}
