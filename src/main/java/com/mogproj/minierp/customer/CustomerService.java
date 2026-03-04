package com.mogproj.minierp.customer;

import com.mogproj.minierp.common.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Customer create(CreateCustomerRequest request) {
        Customer customer = new Customer(request.name(), request.email(), request.phone());
        return repository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return repository.findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer", id));
    }

    @Transactional(readOnly = true)
    public Page<Customer> list(String nameFilter, Pageable pageable) {
        if (nameFilter != null && !nameFilter.isBlank()) {
            return repository.findAll(CustomerSpec.nameContains(nameFilter), pageable);
        }
        return repository.findAllByDeletedAtIsNull(pageable);
    }

    @Transactional
    public Customer update(Long id, UpdateCustomerRequest request) {
        Customer customer = repository.findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer", id));
        customer.update(request.name(), request.email(), request.phone());
        return repository.save(customer);
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = repository.findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer", id));
        customer.softDelete();
        repository.save(customer);
    }
}
