package com.sims.backend.controllers;

import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.sims.backend.services.SettingsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/settings/me/preferences")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class SettingsController {
    private final SettingsService settings;
    @GetMapping public Map<String, String> get(Authentication authentication) { return settings.getUserPreferences(authentication.getName()); }
    @PutMapping public Map<String, String> put(Authentication authentication, @RequestBody Map<String, String> values) { return settings.updateUserPreferences(authentication.getName(), values); }
}
