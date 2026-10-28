package com.sims.backend.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.time.ZoneId;

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
    private static final Map<String, String> DEFAULTS = Map.of("theme", "light", "sidebarCollapsed", "false", "tablePageSize", "25");
    private static final Map<String, String> SCHOOL_DEFAULTS = Map.of(
            "schoolName", "",
            "shortName", "",
            "academicYear", "",
            "timezone", "",
            "language", "");
    private static final long SCHOOL_SETTINGS_OWNER_ID = 1L;
    private static final Set<String> ALLOWED_LANGUAGES = Set.of("English", "Kiswahili", "French");
    private static final Set<String> ALLOWED_THEMES = Set.of("light");
    private static final Set<String> ALLOWED_PAGE_SIZES = Set.of("10", "25", "50", "100");
    private final SettingRepository settings;
    private final UserRepository users;
    private final AuditService audit;

    @Transactional(readOnly = true)
    public Map<String, String> getUserPreferences(String username) {
        UserModel user = users.findByUsername(username).orElseThrow();
        Map<String, String> result = new LinkedHashMap<>(DEFAULTS);
        settings.findByScopeAndOwnerId("USER", user.getUserId()).forEach(setting -> result.put(setting.getKey(), setting.getValue()));
        result.put("theme", "light");
        return result;
    }

    @Transactional
    public Map<String, String> updateUserPreferences(String username, Map<String, String> values) {
        UserModel user = users.findByUsername(username).orElseThrow();
        if (values == null || !values.keySet().equals(DEFAULTS.keySet())) throw new IllegalArgumentException("Provide theme, sidebarCollapsed, and tablePageSize.");
        if (values.get("theme") == null || !ALLOWED_THEMES.contains(values.get("theme"))) throw new IllegalArgumentException("Theme is fixed to light mode.");
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

    @Transactional(readOnly = true)
    public Map<String, String> getSchoolProfile() {
        Map<String, String> result = new LinkedHashMap<>(SCHOOL_DEFAULTS);
        settings.findByScopeAndOwnerId("SCHOOL", SCHOOL_SETTINGS_OWNER_ID)
                .forEach(setting -> result.put(setting.getKey(), setting.getValue()));
        return result;
    }

    @Transactional
    public Map<String, String> updateSchoolProfile(String username, Map<String, String> values) {
        if (values == null || !values.keySet().equals(SCHOOL_DEFAULTS.keySet())) {
            throw new IllegalArgumentException("Provide schoolName, shortName, academicYear, timezone, and language.");
        }
        validateSchoolProfile(values);
        Map<String, String> normalized = new LinkedHashMap<>();
        for (var entry : values.entrySet()) {
            String value = entry.getValue().trim();
            normalized.put(entry.getKey(), value);
            var existing = settings.findByScopeAndOwnerIdAndKey("SCHOOL", SCHOOL_SETTINGS_OWNER_ID, entry.getKey());
            String prior = existing.map(SettingModel::getValue).orElse(SCHOOL_DEFAULTS.get(entry.getKey()));
            if (prior.equals(value)) continue;
            SettingModel setting = existing.orElseGet(
                    () -> new SettingModel("SCHOOL", SCHOOL_SETTINGS_OWNER_ID, entry.getKey(), value));
            setting.setValue(value);
            settings.save(setting);
            audit.record(username, "SCHOOL_PROFILE_UPDATED", "school:1/" + entry.getKey(),
                    "before=" + prior + "; after=" + value);
        }
        return normalized;
    }

    private void validateSchoolProfile(Map<String, String> values) {
        for (String key : SCHOOL_DEFAULTS.keySet()) {
            String value = values.get(key);
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(key + " is required.");
            }
        }
        if (values.get("schoolName").trim().length() > 100) throw new IllegalArgumentException("schoolName must be at most 100 characters.");
        if (values.get("shortName").trim().length() > 20) throw new IllegalArgumentException("shortName must be at most 20 characters.");
        if (values.get("academicYear").trim().length() > 20) throw new IllegalArgumentException("academicYear must be at most 20 characters.");
        if (!ZoneId.getAvailableZoneIds().contains(values.get("timezone").trim())
                && !"UTC".equals(values.get("timezone").trim())) {
            throw new IllegalArgumentException("timezone must be a valid time zone.");
        }
        if (!ALLOWED_LANGUAGES.contains(values.get("language").trim())) {
            throw new IllegalArgumentException("language must be English, Kiswahili, or French.");
        }
    }
}
