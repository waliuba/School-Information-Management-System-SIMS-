package com.sims.backend.dtos.settings;

import java.time.Instant;

public record AuditEventResponse(
        Long eventId,
        Instant occurredAt,
        String actor,
        String action,
        String target) {}
