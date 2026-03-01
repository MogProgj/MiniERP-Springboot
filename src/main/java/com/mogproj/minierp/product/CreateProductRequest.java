package com.mogproj.minierp.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductRequest(
        @NotBlank String sku,
        @NotBlank String name,
        @NotNull @Min(0) Integer priceCents) {
}
