package com.mogproj.minierp.order;

import com.mogproj.minierp.common.exception.BusinessRuleException;
import com.mogproj.minierp.common.exception.EntityNotFoundException;
import com.mogproj.minierp.customer.CustomerRepository;
import com.mogproj.minierp.inventory.Inventory;
import com.mogproj.minierp.inventory.InventoryRepository;
import com.mogproj.minierp.product.Product;
import com.mogproj.minierp.product.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Order createDraft(CreateOrderRequest request) {
        var customer = customerRepository.findActiveById(request.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer", request.customerId()));
        return orderRepository.save(new Order(customer));
    }

    @Transactional
    public Order addItem(Long orderId, AddOrderItemRequest request) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order", orderId));
        if (order.getStatus() != Order.Status.DRAFT) {
            throw new BusinessRuleException(
                    "Cannot add items to a " + order.getStatus() + " order");
        }
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product", request.productId()));
        if (!product.getActive()) {
            throw new BusinessRuleException(
                    "Cannot add inactive product " + product.getId() + " to order");
        }
        order.addItem(new OrderItem(order, product, request.qty(), product.getPriceCents()));
        return orderRepository.save(order);
    }

    @Transactional
    public Order confirmOrder(Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order", orderId));
        if (order.getItems().isEmpty()) {
            throw new BusinessRuleException("Cannot confirm an empty order");
        }
        for (OrderItem item : order.getItems()) {
            Inventory inv = inventoryRepository
                    .findByProductIdForUpdate(item.getProduct().getId())
                    .orElseThrow(() -> new BusinessRuleException(
                            "No inventory record for product " + item.getProduct().getId()));
            inv.adjustStock(-item.getQty());
            inventoryRepository.save(inv);
        }
        try {
            order.confirm();
        } catch (IllegalStateException ex) {
            throw new BusinessRuleException(ex.getMessage());
        }
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order", orderId));
        if (order.getStatus() == Order.Status.CONFIRMED) {
            for (OrderItem item : order.getItems()) {
                inventoryRepository.findByProductIdForUpdate(item.getProduct().getId())
                        .ifPresent(inv -> {
                            inv.adjustStock(item.getQty());
                            inventoryRepository.save(inv);
                        });
            }
        }
        try {
            order.cancel();
        } catch (IllegalStateException ex) {
            throw new BusinessRuleException(ex.getMessage());
        }
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new EntityNotFoundException("Order", id));
    }

    @Transactional(readOnly = true)
    public Page<Order> list(Order.Status status, Pageable pageable) {
        if (status != null) {
            return orderRepository.findByStatus(status, pageable);
        }
        return orderRepository.findAll(pageable);
    }
}
