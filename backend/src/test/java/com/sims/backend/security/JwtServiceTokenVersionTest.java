package com.sims.backend.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class JwtServiceTokenVersionTest {
    @Test
    void embedsTheTokenVersionUsedForRevocationChecks() {
        JwtService service = new JwtService("635LmjiiNkC/lgxvt+Mf6b482jSOEZVRL37PA7UmGQI=", 60_000L);
        String token = service.generateToken("admin", 7L);
        assertEquals(7L, service.extractTokenVersion(token));
    }
}
