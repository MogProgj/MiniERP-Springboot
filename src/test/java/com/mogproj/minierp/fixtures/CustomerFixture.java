package com.mogproj.minierp.fixtures;

import com.mogproj.minierp.customer.Customer;

public class CustomerFixture {

    public static Customer alice() {
        return new Customer("Alice Smith", "alice@example.com", "+1-555-0100");
    }

    public static Customer bob() {
        return new Customer("Bob Jones", "bob@example.com", null);
    }
}
