package com.mogproj.minierp.inventory;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Inventory", description = "Inventory and stock management")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public Page<InventoryResponse> list(Pageable pageable) {
        return service.list(pageable).map(InventoryResponseMapper::toResponse);
    }

    @PostMapping("/{productId}/adjust")
    public InventoryResponse adjust(@PathVariable Long productId,
                                    @Valid @RequestBody AdjustStockRequest request) {
        return InventoryResponseMapper.toResponse(service.adjustStock(productId, request));
    }
}
