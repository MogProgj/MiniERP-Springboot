package com.mogproj.minierp.customer;

import jakarta.validation.constraints.Email;

public record UpdateCustomerRequest(
        String name,
        @Email String email,
        String phone) {
}
