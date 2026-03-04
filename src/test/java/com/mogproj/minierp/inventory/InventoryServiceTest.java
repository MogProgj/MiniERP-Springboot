package com.mogproj.minierp.inventory;

import com.mogproj.minierp.common.exception.EntityNotFoundException;
import com.mogproj.minierp.common.exception.InsufficientInventoryException;
import com.mogproj.minierp.fixtures.ProductFixture;
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
class InventoryServiceTest {

    @Mock
    InventoryRepository repository;

    @InjectMocks
    InventoryService service;

    @Test
    void adjustStock_positiveelta_shouldIncreaseQuantity() {
        var product = ProductFixture.widget();
        var inventory = new Inventory(product, 10, 5);
        when(repository.findByProductIdForUpdate(1L)).thenReturn(Optional.of(inventory));
        when(repository.save(any(Inventory.class))).thenReturn(inventory);

        Inventory result = service.adjustStock(1L, new AdjustStockRequest(5));

        assertThat(result.getQuantityOnHand()).isEqualTo(15);
    }

    @Test
    void adjustStock_negativeDelta_shouldDecreaseQuantity() {
        var product = ProductFixture.widget();
        var inventory = new Inventory(product, 10, 5);
        when(repository.findByProductIdForUpdate(1L)).thenReturn(Optional.of(inventory));
        when(repository.save(any(Inventory.class))).thenReturn(inventory);

        Inventory result = service.adjustStock(1L, new AdjustStockRequest(-3));

        assertThat(result.getQuantityOnHand()).isEqualTo(7);
    }

    @Test
    void adjustStock_belowZero_shouldThrow() {
        var product = ProductFixture.widget();
        var inventory = new Inventory(product, 2, 5);
        when(repository.findByProductIdForUpdate(1L)).thenReturn(Optional.of(inventory));

        assertThatThrownBy(() -> service.adjustStock(1L, new AdjustStockRequest(-10)))
                .isInstanceOf(InsufficientInventoryException.class);
    }

    @Test
    void findByProductId_whenNotFound_shouldThrow() {
        when(repository.findByProductId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByProductId(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
