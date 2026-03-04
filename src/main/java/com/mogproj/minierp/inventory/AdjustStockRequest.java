package com.mogproj.minierp.inventory;

import jakarta.validation.constraints.NotNull;

public record AdjustStockRequest(
        @NotNull Integer delta) {
}
