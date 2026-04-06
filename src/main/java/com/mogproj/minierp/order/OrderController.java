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
    public ResponseEntity<OrderResponse> createDraft(@Valid @RequestBody CreateOrderRequest request) {
        Order order = service.createDraft(request);
        OrderResponse response = OrderResponseMapper.toResponse(order);
        return ResponseEntity.created(URI.create("/orders/" + response.id())).body(response);
    }

    @PostMapping("/{id}/items")
    public OrderResponse addItem(@PathVariable Long id,
                                 @Valid @RequestBody AddOrderItemRequest request) {
        return OrderResponseMapper.toResponse(service.addItem(id, request));
    }

    @PostMapping("/{id}/confirm")
    public OrderResponse confirm(@PathVariable Long id) {
        return OrderResponseMapper.toResponse(service.confirmOrder(id));
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable Long id) {
        return OrderResponseMapper.toResponse(service.cancelOrder(id));
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable Long id) {
        return OrderResponseMapper.toResponse(service.findById(id));
    }

    @GetMapping
    public Page<OrderResponse> list(@RequestParam(required = false) Order.Status status,
                                    Pageable pageable) {
        return service.list(status, pageable).map(OrderResponseMapper::toResponse);
    }
}
