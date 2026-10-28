package com.sims.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sims.backend.models.AuditEventModel;
import com.sims.backend.models.SettingModel;
import com.sims.backend.repositories.AuditEventRepository;
import com.sims.backend.repositories.SettingRepository;
import com.sims.backend.repositories.UserRepository;

class SettingsServiceSchoolProfileTest {
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
    void schoolProfileReadsPersistedValuesWithBlankUnsetValues() {
        when(settings.findByScopeAndOwnerId("SCHOOL", 1L)).thenReturn(java.util.List.of(
                new SettingModel("SCHOOL", 1L, "schoolName", "Example School")));

        Map<String, String> profile = service.getSchoolProfile();

        assertEquals("Example School", profile.get("schoolName"));
        assertEquals("", profile.get("timezone"));
    }

    @Test
    void schoolProfileUpdatePersistsValuesAndAuditsChanges() {
        Map<String, String> values = Map.of(
                "schoolName", " Example School ",
                "shortName", "ES",
                "academicYear", "2026/2027",
                "timezone", "Africa/Nairobi",
                "language", "English");
        when(settings.findByScopeAndOwnerIdAndKey(eq("SCHOOL"), eq(1L), anyString()))
                .thenReturn(Optional.empty());

        Map<String, String> saved = service.updateSchoolProfile("admin", values);

        assertEquals("Example School", saved.get("schoolName"));
        verify(settings, times(5)).save(any(SettingModel.class));
        verify(audit, times(5)).record(eq("admin"), eq("SCHOOL_PROFILE_UPDATED"), anyString(), anyString());
    }

    @Test
    void invalidSchoolProfileDoesNotWriteSettings() {
        Map<String, String> values = Map.of(
                "schoolName", "Example School",
                "shortName", "ES",
                "academicYear", "2026/2027",
                "timezone", "Not/A_Timezone",
                "language", "English");

        assertThrows(IllegalArgumentException.class, () -> service.updateSchoolProfile("admin", values));

        verify(settings, times(0)).save(any(SettingModel.class));
    }

    @Test
    void auditViewerReturnsEventSummaryWithoutDetails() {
        AuditEventRepository repository = mock(AuditEventRepository.class);
        AuditService auditService = new AuditService(repository, null);
        when(repository.findTop100ByOrderByOccurredAtDesc()).thenReturn(java.util.List.of(
                new AuditEventModel("admin", "SCHOOL_PROFILE_UPDATED", "school:1/schoolName",
                        "correlation-id", Instant.parse("2026-10-28T00:00:00Z"), "sensitive details")));

        var result = auditService.getRecentEvents();

        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).actor());
        assertEquals("SCHOOL_PROFILE_UPDATED", result.get(0).action());
        verify(repository).findTop100ByOrderByOccurredAtDesc();
    }
}
