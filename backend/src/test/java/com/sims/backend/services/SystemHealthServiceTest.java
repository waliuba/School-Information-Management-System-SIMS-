package com.sims.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import com.sims.backend.repositories.AuditEventRepository;

class SystemHealthServiceTest {
    @Test
    void reportsAllConnectedChecksAsUp() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        AuditEventRepository auditEvents = mock(AuditEventRepository.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(2)).thenReturn(true);
        when(auditEvents.count()).thenReturn(0L);

        var response = new SystemHealthService(dataSource, auditEvents).getStatus();

        assertEquals("UP", response.status());
        assertEquals(4, response.checks().size());
        assertEquals("UP", response.checks().get(1).status());
    }

    @Test
    void reportsDatabaseUnavailableInsteadOfClaimingHealthy() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        AuditEventRepository auditEvents = mock(AuditEventRepository.class);
        when(dataSource.getConnection()).thenThrow(new SQLException("connection unavailable"));
        when(auditEvents.count()).thenReturn(0L);

        var response = new SystemHealthService(dataSource, auditEvents).getStatus();

        assertEquals("DOWN", response.status());
        assertEquals("DOWN", response.checks().get(1).status());
    }
}
