package com.vehicle.manager.user.repository;

import com.vehicle.manager.user.dao.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository  extends JpaRepository<Template, UUID> {
    Optional<Template> findByName(String name);
}
