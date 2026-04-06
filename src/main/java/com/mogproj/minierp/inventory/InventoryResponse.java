package com.mogproj.minierp.inventory;

import java.time.Instant;

public record InventoryResponse(
        Long productId,
        Integer quantityOnHand,
        Integer reorderPoint,
        Instant updatedAt
) {
}
