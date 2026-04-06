package com.mogproj.minierp.product;

public final class ProductResponseMapper {

    private ProductResponseMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getPriceCents(),
                product.getActive(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }
}
