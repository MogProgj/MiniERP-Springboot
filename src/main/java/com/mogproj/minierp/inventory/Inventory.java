package com.mogproj.minierp.inventory;

import com.mogproj.minierp.common.exception.InsufficientInventoryException;
import com.mogproj.minierp.product.Product;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity_on_hand", nullable = false)
    private Integer quantityOnHand;

    @Column(name = "reorder_point", nullable = false)
    private Integer reorderPoint;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Inventory() {
    }

    public Inventory(Product product, Integer quantityOnHand, Integer reorderPoint) {
        this.product = product;
        this.quantityOnHand = quantityOnHand;
        this.reorderPoint = reorderPoint;
        this.updatedAt = Instant.now();
    }

    public void adjustStock(int delta) {
        int newQty = this.quantityOnHand + delta;
        if (newQty < 0) {
            throw new InsufficientInventoryException(productId, quantityOnHand, Math.abs(delta));
        }
        this.quantityOnHand = newQty;
        this.updatedAt = Instant.now();
    }

    public boolean hasSufficientStock(int required) {
        return this.quantityOnHand >= required;
    }

    public Long getProductId() {
        return productId;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    public Integer getReorderPoint() {
        return reorderPoint;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
