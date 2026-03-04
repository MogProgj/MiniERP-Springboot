package com.mogproj.minierp.product;

import jakarta.validation.constraints.Min;

public record UpdateProductRequest(
        String sku,
        String name,
        @Min(0) Integer priceCents,
        Boolean active) {
}
