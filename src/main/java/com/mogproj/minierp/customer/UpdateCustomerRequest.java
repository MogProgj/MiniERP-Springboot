package com.mogproj.minierp.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateCustomerRequest(
        @Size(min = 1) String name,
        @Email String email,
        String phone) {
}
