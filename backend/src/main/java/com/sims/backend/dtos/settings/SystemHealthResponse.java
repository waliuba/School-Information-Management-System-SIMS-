package com.sims.backend.dtos.settings;

import java.time.Instant;
import java.util.List;

public record SystemHealthResponse(String status, Instant checkedAt, List<SystemHealthCheck> checks) {
}
