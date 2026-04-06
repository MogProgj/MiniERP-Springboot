package com.mogproj.minierp.inventory;

public final class InventoryResponseMapper {

    private InventoryResponseMapper() {
    }

    public static InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getProductId(),
                inventory.getQuantityOnHand(),
                inventory.getReorderPoint(),
                inventory.getUpdatedAt());
    }
}
