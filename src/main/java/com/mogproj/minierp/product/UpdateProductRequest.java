package com.mogproj.minierp.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
        @Size(min = 1) String sku,
        @Size(min = 1) String name,
        @Min(0) Integer priceCents,
        Boolean active) {
}
