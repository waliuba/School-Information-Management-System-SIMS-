package com.sims.backend.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PasswordChangeRateLimiterTest {
    @Test
    void limitsRepeatedPasswordChangeRequestsPerKey() {
        PasswordChangeRateLimiter limiter = new PasswordChangeRateLimiter();
        for (int attempt = 0; attempt < 5; attempt++) assertTrue(limiter.allow("admin:127.0.0.1"));
        assertFalse(limiter.allow("admin:127.0.0.1"));
        assertTrue(limiter.allow("other:127.0.0.1"));
    }
}
