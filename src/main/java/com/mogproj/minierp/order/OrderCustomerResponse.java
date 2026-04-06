package com.mogproj.minierp.order;

public record OrderCustomerResponse(
        Long id,
        String name,
        String email
) {
}
