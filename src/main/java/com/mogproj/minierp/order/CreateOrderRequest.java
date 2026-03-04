package com.mogproj.minierp.order;

import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        @NotNull Long customerId) {
}
