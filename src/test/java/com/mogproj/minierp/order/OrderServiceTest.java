package com.mogproj.minierp.order;

import com.mogproj.minierp.common.exception.BusinessRuleException;
import com.mogproj.minierp.common.exception.EntityNotFoundException;
import com.mogproj.minierp.common.exception.InsufficientInventoryException;
import com.mogproj.minierp.customer.Customer;
import com.mogproj.minierp.customer.CustomerRepository;
import com.mogproj.minierp.fixtures.CustomerFixture;
import com.mogproj.minierp.fixtures.ProductFixture;
import com.mogproj.minierp.inventory.Inventory;
import com.mogproj.minierp.inventory.InventoryRepository;
import com.mogproj.minierp.product.Product;
import com.mogproj.minierp.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    InventoryRepository inventoryRepository;

    @InjectMocks
    OrderService service;

    @Test
    void createDraft_whenCustomerExists_shouldReturnDraftOrder() {
        var customer = CustomerFixture.alice();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        var order = new Order(customer);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = service.createDraft(new CreateOrderRequest(1L));

        assertThat(result.getStatus()).isEqualTo(Order.Status.DRAFT);
    }

    @Test
    void createDraft_whenCustomerNotFound_shouldThrow() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createDraft(new CreateOrderRequest(99L)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void addItem_toNonDraftOrder_shouldThrow() {
        var customer = CustomerFixture.alice();
        var order = new Order(customer);
        order.confirm();
        when(orderRepository.findByIdWithItems(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> service.addItem(1L, new AddOrderItemRequest(1L, 2)))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("CONFIRMED");
    }

    @Test
    void confirmOrder_emptyOrder_shouldThrow() {
        var customer = CustomerFixture.alice();
        var order = new Order(customer);
        when(orderRepository.findByIdWithItems(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> service.confirmOrder(1L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("empty");
    }

    @Test
    void confirmOrder_withInsufficientInventory_shouldThrow() {
        var customer = CustomerFixture.alice();
        var product = ProductFixture.widget();
        var order = new Order(customer);
        var item = new OrderItem(order, product, 10, product.getPriceCents());
        order.addItem(item);

        var inventory = new Inventory(product, 5, 0);

        when(orderRepository.findByIdWithItems(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductIdForUpdate(any()))
                .thenReturn(Optional.of(inventory));

        assertThatThrownBy(() -> service.confirmOrder(1L))
                .isInstanceOf(InsufficientInventoryException.class);
    }
}
