package com.mogproj.minierp.customer;

import com.mogproj.minierp.common.exception.EntityNotFoundException;
import com.mogproj.minierp.fixtures.CustomerFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository repository;

    @InjectMocks
    CustomerService service;

    @Test
    void create_shouldPersistAndReturnCustomer() {
        var request = new CreateCustomerRequest("Alice", "alice@example.com", null);
        var saved = CustomerFixture.alice();
        when(repository.save(any(Customer.class))).thenReturn(saved);

        Customer result = service.create(request);

        assertThat(result.getName()).isEqualTo("Alice Smith");
        verify(repository).save(any(Customer.class));
    }

    @Test
    void findById_whenExists_shouldReturnCustomer() {
        var customer = CustomerFixture.alice();
        when(repository.findActiveById(1L)).thenReturn(Optional.of(customer));

        Customer result = service.findById(1L);

        assertThat(result.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void findById_whenNotFound_shouldThrowEntityNotFoundException() {
        when(repository.findActiveById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void update_shouldModifyFieldsAndReturn() {
        var customer = CustomerFixture.alice();
        when(repository.findActiveById(1L)).thenReturn(Optional.of(customer));
        when(repository.save(any(Customer.class))).thenReturn(customer);

        var request = new UpdateCustomerRequest("Alice Updated", null, null);
        Customer result = service.update(1L, request);

        assertThat(result.getName()).isEqualTo("Alice Updated");
    }

    @Test
    void delete_shouldSoftDeleteCustomer() {
        var customer = CustomerFixture.alice();
        when(repository.findActiveById(1L)).thenReturn(Optional.of(customer));
        when(repository.save(any(Customer.class))).thenReturn(customer);

        service.delete(1L);

        assertThat(customer.isDeleted()).isTrue();
        verify(repository).save(customer);
    }
}
