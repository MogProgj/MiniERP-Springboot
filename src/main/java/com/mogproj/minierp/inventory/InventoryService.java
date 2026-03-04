package com.mogproj.minierp.inventory;

import com.mogproj.minierp.common.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Inventory> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Inventory findByProductId(Long productId) {
        return repository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory", productId));
    }

    @Transactional
    public Inventory adjustStock(Long productId, AdjustStockRequest request) {
        Inventory inventory = repository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory", productId));
        inventory.adjustStock(request.delta());
        return repository.save(inventory);
    }
}
