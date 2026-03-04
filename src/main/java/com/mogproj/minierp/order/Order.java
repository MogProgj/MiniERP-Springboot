package com.mogproj.minierp.order;

import com.mogproj.minierp.customer.Customer;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    public enum Status {
        DRAFT, CONFIRMED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected Order() {
    }

    public Order(Customer customer) {
        this.customer = customer;
        this.status = Status.DRAFT;
        this.createdAt = Instant.now();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void confirm() {
        if (this.status != Status.DRAFT) {
            throw new IllegalStateException("Only DRAFT orders can be confirmed");
        }
        this.status = Status.CONFIRMED;
    }

    public void cancel() {
        if (this.status == Status.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }
        this.status = Status.CANCELLED;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Status getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
