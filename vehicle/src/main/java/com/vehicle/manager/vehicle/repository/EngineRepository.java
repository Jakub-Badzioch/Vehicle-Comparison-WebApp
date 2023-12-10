package com.vehicle.manager.vehicle.repository;

import com.vehicle.manager.vehicle.dao.Engine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EngineRepository extends JpaRepository<Engine, UUID> {
    void deleteByIdIn(List<UUID> list);
}
