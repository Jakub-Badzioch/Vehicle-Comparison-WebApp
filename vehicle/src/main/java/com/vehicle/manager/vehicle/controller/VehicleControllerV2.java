package com.vehicle.manager.vehicle.controller;

import com.vehicle.manager.commons.dto.FilteringAndPagingDTO;
import com.vehicle.manager.commons.dto.model.VehicleDTO;
import com.vehicle.manager.vehicle.mapper.VehicleMapper;
import com.vehicle.manager.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles/v2")
public class VehicleControllerV2 {

    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;

    @PostMapping("/search")
    @Deprecated
    public Page<VehicleDTO> filterVehicles(@Valid @RequestBody FilteringAndPagingDTO filteringAndPagingDTO) {
        return vehicleService.filter(filteringAndPagingDTO)
                .map(vehicleMapper::toThinDTO);
    }

    @GetMapping("/{id}")
    public VehicleDTO getProductById(@PathVariable UUID id) {
        return vehicleMapper.toDTO(vehicleService.getById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-yours")
    public Page<VehicleDTO> getAllVehiclesCreatedByYou(@RequestParam int pageNumber, @RequestParam  int pageSize) {
        return vehicleService.getAllVehiclesCreatedByYou(pageNumber, pageSize)
                .map(vehicleMapper::toThinDTO);
    }
}
