package com.ms.inventory.repo;

import com.ms.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findBySkewCode(String skewCode);
}
