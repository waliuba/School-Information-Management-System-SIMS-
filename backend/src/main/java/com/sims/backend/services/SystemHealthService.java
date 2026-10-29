package com.sims.backend.services;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.sims.backend.dtos.settings.SystemHealthCheck;
import com.sims.backend.dtos.settings.SystemHealthResponse;
import com.sims.backend.repositories.AuditEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemHealthService {
    private final DataSource dataSource;
    private final AuditEventRepository auditEvents;

    public SystemHealthResponse getStatus() {
        String databaseStatus = getDatabaseStatus();
        String auditStatus = getAuditStorageStatus();
        List<SystemHealthCheck> checks = List.of(
                new SystemHealthCheck("Application API", "UP", "The backend API is responding."),
                new SystemHealthCheck("Oracle database", databaseStatus,
                        databaseStatus.equals("UP") ? "A database connection is available." : "The database connection is unavailable."),
                new SystemHealthCheck("Authentication", "UP", "This request was accepted by the authenticated API."),
                new SystemHealthCheck("Audit storage", auditStatus,
                        auditStatus.equals("UP") ? "Audit records can be read." : "Audit storage is unavailable."));
        String status = checks.stream().allMatch(check -> check.status().equals("UP")) ? "UP" : "DOWN";
        return new SystemHealthResponse(status, Instant.now(), checks);
    }

    private String getDatabaseStatus() {
        try (var connection = dataSource.getConnection()) {
            return connection.isValid(2) ? "UP" : "DOWN";
        } catch (SQLException ex) {
            return "DOWN";
        }
    }

    private String getAuditStorageStatus() {
        try {
            auditEvents.count();
            return "UP";
        } catch (DataAccessException ex) {
            return "DOWN";
        }
    }
}
