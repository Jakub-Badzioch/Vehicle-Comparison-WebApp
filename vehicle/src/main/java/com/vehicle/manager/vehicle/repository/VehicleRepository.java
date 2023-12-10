package com.vehicle.manager.vehicle.repository;

import com.vehicle.manager.vehicle.dao.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID>, JpaSpecificationExecutor<Vehicle> {
    boolean existsByIdAndCreatedBy(UUID id, String email);

    Page<Vehicle> findByCreatedBy(String email, Pageable pageable);
}
