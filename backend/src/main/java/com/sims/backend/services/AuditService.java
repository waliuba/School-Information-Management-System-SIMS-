package com.sims.backend.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.sims.backend.models.AuditEventModel;
import com.sims.backend.repositories.AuditEventRepository;
import com.sims.backend.dtos.settings.AuditEventResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditEventRepository repository;
    private final HttpServletRequest request;

    public void record(String actor, String action, String target, String redactedDetails) {
        String correlationId = request.getHeader("X-Correlation-ID");
        if (correlationId == null || !correlationId.matches("[A-Za-z0-9._-]{1,64}")) correlationId = UUID.randomUUID().toString();
        repository.save(new AuditEventModel(actor, action, target, correlationId, Instant.now(), redactedDetails));
    }

    public List<AuditEventResponse> getRecentEvents() {
        return repository.findTop100ByOrderByOccurredAtDesc().stream()
                .map(event -> new AuditEventResponse(
                        event.getEventId(),
                        event.getOccurredAt(),
                        event.getActor(),
                        event.getAction(),
                        event.getTarget()))
                .toList();
    }
}
