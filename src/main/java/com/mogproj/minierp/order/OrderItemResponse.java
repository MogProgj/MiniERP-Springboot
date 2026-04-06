package com.mogproj.minierp.order;

public record OrderItemResponse(
        Long productId,
        Integer qty,
        Integer unitPriceCents,
        OrderItemProductResponse product
) {
}
