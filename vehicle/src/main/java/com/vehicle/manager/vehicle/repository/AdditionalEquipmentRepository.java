package com.vehicle.manager.vehicle.repository;

import com.vehicle.manager.vehicle.dao.AdditionalEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdditionalEquipmentRepository extends JpaRepository<AdditionalEquipment, UUID> {
}
