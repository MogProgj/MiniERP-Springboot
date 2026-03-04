package com.mogproj.minierp.product;

import com.mogproj.minierp.common.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Product create(CreateProductRequest request) {
        Product product = new Product(request.sku(), request.name(), request.priceCents());
        return repository.save(product);
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
    }

    @Transactional(readOnly = true)
    public Page<Product> list(String nameFilter, String skuFilter, Boolean active, Pageable pageable) {
        Specification<Product> spec = Specification.where((Specification<Product>) null);
        if (nameFilter != null && !nameFilter.isBlank()) {
            spec = spec.and(ProductSpec.nameContains(nameFilter));
        }
        if (skuFilter != null && !skuFilter.isBlank()) {
            spec = spec.and(ProductSpec.skuContains(skuFilter));
        }
        if (active != null) {
            spec = spec.and(ProductSpec.isActive(active));
        }
        return repository.findAll(spec, pageable);
    }

    @Transactional
    public Product update(Long id, UpdateProductRequest request) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
        product.update(request.sku(), request.name(), request.priceCents(), request.active());
        return repository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
        product.update(null, null, null, false);
        repository.save(product);
    }
}
