package com.ms.inventory.service;

import com.ms.inventory.dto.InventoryResponse;
import com.ms.inventory.repo.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepo inventoryRepo;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skewCode) {
        return inventoryRepo.findBySkewCodeIn(skewCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skewCode(inventory.getSkewCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
