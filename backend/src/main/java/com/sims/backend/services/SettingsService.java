package com.sims.backend.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sims.backend.models.SettingModel;
import com.sims.backend.models.UserModel;
import com.sims.backend.repositories.SettingRepository;
import com.sims.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingsService {
    private static final Map<String, String> DEFAULTS = Map.of("theme", "system", "sidebarCollapsed", "false", "tablePageSize", "25");
    private static final Set<String> ALLOWED_THEMES = Set.of("light", "dark", "system");
    private static final Set<String> ALLOWED_PAGE_SIZES = Set.of("10", "25", "50", "100");
    private final SettingRepository settings;
    private final UserRepository users;
    private final AuditService audit;

    @Transactional(readOnly = true)
    public Map<String, String> getUserPreferences(String username) {
        UserModel user = users.findByUsername(username).orElseThrow();
        Map<String, String> result = new LinkedHashMap<>(DEFAULTS);
        settings.findByScopeAndOwnerId("USER", user.getUserId()).forEach(setting -> result.put(setting.getKey(), setting.getValue()));
        return result;
    }

    @Transactional
    public Map<String, String> updateUserPreferences(String username, Map<String, String> values) {
        UserModel user = users.findByUsername(username).orElseThrow();
        if (values == null || !values.keySet().equals(DEFAULTS.keySet())) throw new IllegalArgumentException("Provide theme, sidebarCollapsed, and tablePageSize.");
        if (values.get("theme") == null || !ALLOWED_THEMES.contains(values.get("theme"))) throw new IllegalArgumentException("Theme must be light, dark, or system.");
        if (values.get("sidebarCollapsed") == null || !Set.of("true", "false").contains(values.get("sidebarCollapsed"))) throw new IllegalArgumentException("sidebarCollapsed must be true or false.");
        if (values.get("tablePageSize") == null || !ALLOWED_PAGE_SIZES.contains(values.get("tablePageSize"))) throw new IllegalArgumentException("tablePageSize must be 10, 25, 50, or 100.");
        for (var entry : values.entrySet()) {
            var existing = settings.findByScopeAndOwnerIdAndKey("USER", user.getUserId(), entry.getKey());
            String prior = existing.map(SettingModel::getValue).orElse(DEFAULTS.get(entry.getKey()));
            if (prior.equals(entry.getValue())) continue;
            if (DEFAULTS.get(entry.getKey()).equals(entry.getValue())) {
                existing.ifPresent(settings::delete);
                audit.record(username, "USER_PREFERENCE_RESET", "user:" + user.getUserId() + "/" + entry.getKey(),
                        "before=" + prior + "; after=default");
                continue;
            }
            SettingModel setting = existing.orElseGet(() -> new SettingModel("USER", user.getUserId(), entry.getKey(), entry.getValue()));
            setting.setValue(entry.getValue());
            settings.save(setting);
            audit.record(username, "USER_PREFERENCE_UPDATED", "user:" + user.getUserId() + "/" + entry.getKey(),
                    "before=" + prior + "; after=" + entry.getValue());
        }
        return new LinkedHashMap<>(values);
    }
}
