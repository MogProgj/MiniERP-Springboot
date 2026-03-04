package com.mogproj.minierp.customer;

import org.springframework.data.jpa.domain.Specification;

public class CustomerSpec {

    private CustomerSpec() {
    }

    private static final char ESCAPE_CHAR = '\\';

    public static Specification<Customer> nameContains(String name) {
        String escaped = escapeWildcards(name.toLowerCase());
        return (root, query, cb) -> cb.and(
                cb.like(cb.lower(root.get("name")), "%" + escaped + "%", ESCAPE_CHAR),
                cb.isNull(root.get("deletedAt")));
    }

    private static String escapeWildcards(String value) {
        return value.replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
    }
}
