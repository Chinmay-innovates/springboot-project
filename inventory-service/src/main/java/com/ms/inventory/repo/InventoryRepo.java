package com.ms.inventory.repo;

import com.ms.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    List<Inventory> findBySkewCodeIn(List<String> skewCode);
}
