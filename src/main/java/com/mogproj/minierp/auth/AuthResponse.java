package com.mogproj.minierp.auth;

import java.time.Instant;

public record AuthResponse(
        String token,
        String username,
        String role,
        Instant expiresAt) {
}
