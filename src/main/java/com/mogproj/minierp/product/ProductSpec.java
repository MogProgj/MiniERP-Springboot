package com.mogproj.minierp.product;

import org.springframework.data.jpa.domain.Specification;

public class ProductSpec {

    private ProductSpec() {
    }

    public static Specification<Product> nameContains(String name) {
        String escaped = escapeWildcards(name.toLowerCase());
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + escaped + "%");
    }

    public static Specification<Product> skuContains(String sku) {
        String escaped = escapeWildcards(sku.toLowerCase());
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("sku")), "%" + escaped + "%");
    }

    private static String escapeWildcards(String value) {
        return value.replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
    }

    public static Specification<Product> isActive(Boolean active) {
        return (root, query, cb) -> cb.equal(root.get("active"), active);
    }
}
