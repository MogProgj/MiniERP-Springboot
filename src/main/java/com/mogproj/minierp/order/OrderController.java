package com.mogproj.minierp.order;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Orders", description = "Order lifecycle management")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Order> createDraft(@Valid @RequestBody CreateOrderRequest request) {
        Order order = service.createDraft(request);
        return ResponseEntity.created(URI.create("/orders/" + order.getId())).body(order);
    }

    @PostMapping("/{id}/items")
    public Order addItem(@PathVariable Long id,
                         @Valid @RequestBody AddOrderItemRequest request) {
        return service.addItem(id, request);
    }

    @PostMapping("/{id}/confirm")
    public Order confirm(@PathVariable Long id) {
        return service.confirmOrder(id);
    }

    @PostMapping("/{id}/cancel")
    public Order cancel(@PathVariable Long id) {
        return service.cancelOrder(id);
    }

    @GetMapping("/{id}")
    public Order findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public Page<Order> list(@RequestParam(required = false) Order.Status status,
                            Pageable pageable) {
        return service.list(status, pageable);
    }
}
