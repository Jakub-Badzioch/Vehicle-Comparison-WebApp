package com.vehicle.manager.vehicle.repository;

import com.vehicle.manager.vehicle.dao.Body;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BodyRepository extends JpaRepository<Body, UUID> {
}
