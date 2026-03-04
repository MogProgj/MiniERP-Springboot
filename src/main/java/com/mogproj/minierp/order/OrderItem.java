package com.mogproj.minierp.order;

import com.mogproj.minierp.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @EmbeddedId
    private OrderItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "unit_price_cents", nullable = false)
    private Integer unitPriceCents;

    protected OrderItem() {
    }

    public OrderItem(Order order, Product product, Integer qty, Integer unitPriceCents) {
        this.id = new OrderItemId(order.getId(), product.getId());
        this.order = order;
        this.product = product;
        this.qty = qty;
        this.unitPriceCents = unitPriceCents;
    }

    public OrderItemId getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQty() {
        return qty;
    }

    public Integer getUnitPriceCents() {
        return unitPriceCents;
    }
}
