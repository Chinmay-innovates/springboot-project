package com.ms.inventory.controller;

import com.ms.inventory.dto.InventoryResponse;
import com.ms.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    // http://locahost:8082/api/inventory?skewCode=PROD001&skewCode=PROD002
    public List<InventoryResponse> isInStock(@RequestParam List<String> skewCode) {
        return inventoryService.isInStock(skewCode);
    }
}
