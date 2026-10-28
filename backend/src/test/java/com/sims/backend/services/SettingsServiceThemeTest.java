package com.sims.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sims.backend.models.SettingModel;
import com.sims.backend.models.UserModel;
import com.sims.backend.repositories.SettingRepository;
import com.sims.backend.repositories.UserRepository;

class SettingsServiceThemeTest {
    private SettingRepository settings;
    private UserRepository users;
    private AuditService audit;
    private SettingsService service;

    @BeforeEach
    void setUp() {
        settings = mock(SettingRepository.class);
        users = mock(UserRepository.class);
        audit = mock(AuditService.class);
        service = new SettingsService(settings, users, audit);

        UserModel user = new UserModel();
        user.setUserId(7L);
        user.setUsername("admin");
        when(users.findByUsername("admin")).thenReturn(Optional.of(user));
    }

    @Test
    void preferenceReadsForceLightEvenWhenAnOldDarkThemeIsStored() {
        when(settings.findByScopeAndOwnerId("USER", 7L)).thenReturn(List.of(
                new SettingModel("USER", 7L, "theme", "dark")));

        Map<String, String> preferences = service.getUserPreferences("admin");

        assertEquals("light", preferences.get("theme"));
    }

    @Test
    void darkThemeUpdatesAreRejected() {
        Map<String, String> values = Map.of(
                "theme", "dark",
                "sidebarCollapsed", "false",
                "tablePageSize", "25");

        assertThrows(IllegalArgumentException.class, () -> service.updateUserPreferences("admin", values));

        verifyNoInteractions(settings, audit);
    }

    @Test
    void sendingLightThemeRemovesAnOldDarkOverride() {
        SettingModel oldTheme = new SettingModel("USER", 7L, "theme", "dark");
        when(settings.findByScopeAndOwnerIdAndKey("USER", 7L, "theme")).thenReturn(Optional.of(oldTheme));
        when(settings.findByScopeAndOwnerIdAndKey("USER", 7L, "sidebarCollapsed")).thenReturn(Optional.empty());
        when(settings.findByScopeAndOwnerIdAndKey("USER", 7L, "tablePageSize")).thenReturn(Optional.empty());
        Map<String, String> values = Map.of(
                "theme", "light",
                "sidebarCollapsed", "false",
                "tablePageSize", "25");

        Map<String, String> saved = service.updateUserPreferences("admin", values);

        assertEquals("light", saved.get("theme"));
        verify(settings).delete(oldTheme);
    }
}
