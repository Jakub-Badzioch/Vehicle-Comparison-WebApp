package com.vehicle.manager.vehicle.repository;

import com.vehicle.manager.vehicle.dao.EuroNCAP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EuroNCAPRepository extends JpaRepository<EuroNCAP, UUID> {
}
