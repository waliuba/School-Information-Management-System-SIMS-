package com.sims.backend.security;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

@Component
public class PasswordChangeRateLimiter {
    private static final int LIMIT = 5;
    private static final Duration WINDOW = Duration.ofMinutes(15);
    private final ConcurrentMap<String, Window> windows = new ConcurrentHashMap<>();

    public boolean allow(String key) {
        Instant now = Instant.now();
        if (!windows.containsKey(key) && windows.size() >= 10_000) {
            windows.entrySet().removeIf(entry -> entry.getValue().started().plus(WINDOW).isBefore(now));
            if (windows.size() >= 10_000) return false;
        }
        Window window = windows.compute(key, (ignored, current) -> current == null || current.started().plus(WINDOW).isBefore(now)
                ? new Window(now, 1) : new Window(current.started(), current.count() + 1));
        if (window.count() > LIMIT) return false;
        if (windows.size() > 10_000) windows.entrySet().removeIf(entry -> entry.getValue().started().plus(WINDOW).isBefore(now));
        return true;
    }

    private record Window(Instant started, int count) {}
}
