package com.mogproj.minierp.customer;

import java.time.Instant;

public record CustomerResponse(
        Long id,
        String name,
        String email,
        String phone,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
}
