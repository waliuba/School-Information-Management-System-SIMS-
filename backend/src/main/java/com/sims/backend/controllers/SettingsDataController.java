package com.sims.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sims.backend.dtos.settings.AuditEventResponse;
import com.sims.backend.services.AuditService;
import com.sims.backend.services.SettingsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class SettingsDataController {
    private final SettingsService settings;
    private final AuditService audit;

    @GetMapping("/school-profile")
    public Map<String, String> getSchoolProfile() {
        return settings.getSchoolProfile();
    }

    @PutMapping("/school-profile")
    public Map<String, String> updateSchoolProfile(
            Authentication authentication,
            @RequestBody Map<String, String> values) {
        return settings.updateSchoolProfile(authentication.getName(), values);
    }

    @GetMapping("/audit")
    public List<AuditEventResponse> getAuditEvents() {
        return audit.getRecentEvents();
    }
}
