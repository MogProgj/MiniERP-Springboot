package com.mogproj.minierp.order;

public record OrderItemProductResponse(
        Long id,
        String sku,
        String name
) {
}
