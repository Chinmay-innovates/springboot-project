package com.ms.inventory.controller;

import com.ms.inventory.repo.InventoryRepo;
import com.ms.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{skew-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("skew-code") String skewCode) {
        return inventoryService.isInStock(skewCode);
    }
}
