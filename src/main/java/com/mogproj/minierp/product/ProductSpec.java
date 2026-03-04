package com.mogproj.minierp.product;

import org.springframework.data.jpa.domain.Specification;

public class ProductSpec {

    private ProductSpec() {
    }

    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> skuContains(String sku) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("sku")), "%" + sku.toLowerCase() + "%");
    }

    public static Specification<Product> isActive(Boolean active) {
        return (root, query, cb) -> cb.equal(root.get("active"), active);
    }
}
