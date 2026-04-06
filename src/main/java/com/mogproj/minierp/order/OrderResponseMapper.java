package com.mogproj.minierp.order;

import java.util.List;

public final class OrderResponseMapper {

    private OrderResponseMapper() {
    }

    public static OrderResponse toResponse(Order order) {
        OrderCustomerResponse customer = new OrderCustomerResponse(
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail());

        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getQty(),
                        item.getUnitPriceCents(),
                        new OrderItemProductResponse(
                                item.getProduct().getId(),
                                item.getProduct().getSku(),
                                item.getProduct().getName())))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getStatus().name(),
                order.getCreatedAt(),
                customer,
                items);
    }
}
