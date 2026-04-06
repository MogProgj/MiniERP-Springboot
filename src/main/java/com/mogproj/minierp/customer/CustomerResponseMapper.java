package com.mogproj.minierp.customer;

public final class CustomerResponseMapper {

    private CustomerResponseMapper() {
    }

    public static CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                customer.getDeletedAt());
    }
}
