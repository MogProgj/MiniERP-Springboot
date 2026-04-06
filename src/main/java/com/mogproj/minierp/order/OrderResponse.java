package com.mogproj.minierp.order;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        String status,
        Instant createdAt,
        OrderCustomerResponse customer,
        List<OrderItemResponse> items
) {
}
