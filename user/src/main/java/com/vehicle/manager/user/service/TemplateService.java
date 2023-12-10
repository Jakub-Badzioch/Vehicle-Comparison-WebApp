package com.vehicle.manager.user.service;

import com.vehicle.manager.user.dao.Template;
import com.vehicle.manager.user.repository.TemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;

    public Template getByName(String name) {
        return templateRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException(name));
    }
}
