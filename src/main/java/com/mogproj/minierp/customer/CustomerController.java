package com.mogproj.minierp.customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Customers", description = "Customer management")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody CreateCustomerRequest request) {
        Customer customer = service.create(request);
        return ResponseEntity.created(URI.create("/customers/" + customer.getId())).body(customer);
    }

    @GetMapping("/{id}")
    public Customer findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public Page<Customer> list(@RequestParam(required = false) String name,
                               Pageable pageable) {
        return service.list(name, pageable);
    }

    @PatchMapping("/{id}")
    public Customer update(@PathVariable Long id,
                           @Valid @RequestBody UpdateCustomerRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
