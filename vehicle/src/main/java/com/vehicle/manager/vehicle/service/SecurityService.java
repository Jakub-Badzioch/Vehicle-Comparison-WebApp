package com.vehicle.manager.vehicle.service;

import com.vehicle.manager.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final VehicleRepository vehicleRepository;

    public boolean hasAccessToVehicle(UUID id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return vehicleRepository.existsByIdAndCreatedBy(id, email);
    }
}
