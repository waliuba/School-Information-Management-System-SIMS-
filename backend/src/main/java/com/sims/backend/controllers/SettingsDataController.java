package com.sims.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sims.backend.dtos.settings.AuditEventResponse;
import com.sims.backend.dtos.settings.SystemHealthResponse;
import com.sims.backend.dtos.settings.SystemSettingResponse;
import com.sims.backend.services.AuditService;
import com.sims.backend.services.SettingsService;
import com.sims.backend.services.SystemHealthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class SettingsDataController {
    private final SettingsService settings;
    private final AuditService audit;
    private final SystemHealthService health;

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

    @GetMapping("/system")
    public List<SystemSettingResponse> getSystemSettings() {
        return settings.getSystemSettings();
    }

    @PostMapping("/system")
    @ResponseStatus(HttpStatus.CREATED)
    public SystemSettingResponse createSystemSetting(
            Authentication authentication,
            @RequestBody Map<String, String> value) {
        return settings.createSystemSetting(authentication.getName(), value);
    }

    @PutMapping("/system/{key}")
    public SystemSettingResponse updateSystemSetting(
            Authentication authentication,
            @PathVariable String key,
            @RequestBody Map<String, String> value) {
        return settings.updateSystemSetting(authentication.getName(), key, value);
    }

    @DeleteMapping("/system/{key}")
    public void deleteSystemSetting(Authentication authentication, @PathVariable String key) {
        settings.deleteSystemSetting(authentication.getName(), key);
    }

    @GetMapping("/system-health")
    public SystemHealthResponse getSystemHealth() {
        return health.getStatus();
    }
}
