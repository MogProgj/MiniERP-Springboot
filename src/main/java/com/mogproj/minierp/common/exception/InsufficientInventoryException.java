package com.mogproj.minierp.common.exception;

public class InsufficientInventoryException extends RuntimeException {

    private final Long productId;
    private final int available;
    private final int requested;

    public InsufficientInventoryException(Long productId, int available, int requested) {
        super("Insufficient inventory for product " + productId
                + ": available=" + available + ", requested=" + requested);
        this.productId = productId;
        this.available = available;
        this.requested = requested;
    }

    public Long getProductId() {
        return productId;
    }

    public int getAvailable() {
        return available;
    }

    public int getRequested() {
        return requested;
    }
}
