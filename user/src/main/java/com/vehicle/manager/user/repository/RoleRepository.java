package com.vehicle.manager.user.repository;

import com.vehicle.manager.user.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role getByName(String name);
}
