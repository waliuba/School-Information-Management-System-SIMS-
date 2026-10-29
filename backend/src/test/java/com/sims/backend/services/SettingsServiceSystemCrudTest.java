package com.sims.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sims.backend.exceptions.BusinessRuleException;
import com.sims.backend.exceptions.ResourceNotFoundException;
import com.sims.backend.models.SettingModel;
import com.sims.backend.repositories.SettingRepository;
import com.sims.backend.repositories.UserRepository;

class SettingsServiceSystemCrudTest {
    private SettingRepository settings;
    private AuditService audit;
    private SettingsService service;

    @BeforeEach
    void setUp() {
        settings = mock(SettingRepository.class);
        audit = mock(AuditService.class);
        service = new SettingsService(settings, mock(UserRepository.class), audit);
    }

    @Test
    void listsSystemSettingsInKeyOrder() {
        when(settings.findByScopeAndOwnerId("SYSTEM", 1L)).thenReturn(List.of(
                new SettingModel("SYSTEM", 1L, "zeta", "last"),
                new SettingModel("SYSTEM", 1L, "alpha", "first")));

        var result = service.getSystemSettings();

        assertEquals(List.of("alpha", "zeta"), result.stream().map(setting -> setting.key()).toList());
    }

    @Test
    void createsAndAuditsSystemSetting() {
        when(settings.findByScopeAndOwnerIdAndKey("SYSTEM", 1L, "school.code")).thenReturn(Optional.empty());

        var result = service.createSystemSetting("admin", Map.of("key", "school.code", "value", " ABC "));

        assertEquals("ABC", result.value());
        verify(settings).save(any(SettingModel.class));
        verify(audit).record("admin", "SYSTEM_SETTING_CREATED", "system:school.code", "key=school.code");
    }

    @Test
    void duplicateCreateIsRejectedWithoutWriting() {
        when(settings.findByScopeAndOwnerIdAndKey("SYSTEM", 1L, "school.code"))
                .thenReturn(Optional.of(new SettingModel("SYSTEM", 1L, "school.code", "ABC")));

        assertThrows(BusinessRuleException.class,
                () -> service.createSystemSetting("admin", Map.of("key", "school.code", "value", "XYZ")));

        verifyNoInteractions(audit);
    }

    @Test
    void updateChangesAndAuditsOnlyTheExistingSystemSetting() {
        SettingModel setting = new SettingModel("SYSTEM", 1L, "school.code", "ABC");
        when(settings.findByScopeAndOwnerIdAndKey("SYSTEM", 1L, "school.code")).thenReturn(Optional.of(setting));

        var result = service.updateSystemSetting("admin", "school.code", Map.of("value", "XYZ"));

        assertEquals("XYZ", result.value());
        verify(settings).save(setting);
        verify(audit).record("admin", "SYSTEM_SETTING_UPDATED", "system:school.code", "key=school.code");
    }

    @Test
    void updateMissingSettingReturnsNotFound() {
        when(settings.findByScopeAndOwnerIdAndKey(eq("SYSTEM"), eq(1L), eq("missing")))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateSystemSetting("admin", "missing", Map.of("value", "x")));
    }

    @Test
    void deleteRemovesAndAuditsTheSetting() {
        SettingModel setting = new SettingModel("SYSTEM", 1L, "school.code", "ABC");
        when(settings.findByScopeAndOwnerIdAndKey("SYSTEM", 1L, "school.code")).thenReturn(Optional.of(setting));

        service.deleteSystemSetting("admin", "school.code");

        verify(settings).delete(setting);
        verify(audit).record("admin", "SYSTEM_SETTING_DELETED", "system:school.code", "key=school.code");
    }

    @Test
    void rejectsInvalidKeysBeforeDatabaseAccess() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createSystemSetting("admin", Map.of("key", "../password", "value", "secret")));

        verifyNoInteractions(settings, audit);
    }
}
