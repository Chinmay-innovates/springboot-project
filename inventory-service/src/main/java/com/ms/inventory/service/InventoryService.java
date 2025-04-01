package com.ms.inventory.service;

import com.ms.inventory.repo.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepo inventoryRepo;

    @Transactional(readOnly = true)
    public boolean isInStock(String skewCode) {
        return inventoryRepo.findBySkewCode(skewCode).isPresent();
    }
}
