package com.mogproj.minierp.fixtures;

import com.mogproj.minierp.product.Product;

public class ProductFixture {

    public static Product widget() {
        return new Product("SKU-001", "Widget", 999);
    }

    public static Product gadget() {
        return new Product("SKU-002", "Gadget", 4999);
    }
}
