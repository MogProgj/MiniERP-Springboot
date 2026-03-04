package com.mogproj.minierp.customer;

import org.springframework.data.jpa.domain.Specification;

public class CustomerSpec {

    private CustomerSpec() {
    }

    public static Specification<Customer> nameContains(String name) {
        return (root, query, cb) -> cb.and(
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"),
                cb.isNull(root.get("deletedAt")));
    }
}
