package com.mogproj.minierp.product;

import java.time.Instant;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        Integer priceCents,
        Boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
