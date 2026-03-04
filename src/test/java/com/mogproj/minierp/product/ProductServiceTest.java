package com.mogproj.minierp.product;

import com.mogproj.minierp.common.exception.EntityNotFoundException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository repository;

    @InjectMocks
    ProductService service;

    @Test
    void create_shouldPersistAndReturnProduct() {
        var request = new CreateProductRequest("SKU-001", "Widget", 999);
        var saved = ProductFixture.widget();
        when(repository.save(any(Product.class))).thenReturn(saved);

        Product result = service.create(request);

        assertThat(result.getSku()).isEqualTo("SKU-001");
        verify(repository).save(any(Product.class));
    }

    @Test
    void findById_whenExists_shouldReturnProduct() {
        when(repository.findById(1L)).thenReturn(Optional.of(ProductFixture.widget()));

        Product result = service.findById(1L);

        assertThat(result.getName()).isEqualTo("Widget");
    }

    @Test
    void findById_whenNotFound_shouldThrowEntityNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void update_shouldModifyProduct() {
        var product = ProductFixture.widget();
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenReturn(product);

        var request = new UpdateProductRequest(null, "Widget Pro", null, null);
        Product result = service.update(1L, request);

        assertThat(result.getName()).isEqualTo("Widget Pro");
    }

    @Test
    void delete_shouldDeactivateProduct() {
        var product = ProductFixture.widget();
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenReturn(product);

        service.delete(1L);

        assertThat(product.getActive()).isFalse();
        verify(repository).save(product);
    }
}
