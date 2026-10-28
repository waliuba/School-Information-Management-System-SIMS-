package com.sims.backend.models;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "SIMS_AUDIT_EVENT")
public class AuditEventModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID") private Long eventId;
    @Column(name = "ACTOR", nullable = false, length = 100) private String actor;
    @Column(name = "ACTION", nullable = false, length = 80) private String action;
    @Column(name = "TARGET", nullable = false, length = 160) private String target;
    @Column(name = "CORRELATION_ID", nullable = false, length = 64) private String correlationId;
    @Column(name = "OCCURRED_AT", nullable = false) private Instant occurredAt;
    @Lob @Column(name = "DETAILS") private String details;

    protected AuditEventModel() {}

    public AuditEventModel(String actor, String action, String target, String correlationId, Instant occurredAt, String details) {
        this.actor = actor;
        this.action = action;
        this.target = target;
        this.correlationId = correlationId;
        this.occurredAt = occurredAt;
        this.details = details;
    }
}
