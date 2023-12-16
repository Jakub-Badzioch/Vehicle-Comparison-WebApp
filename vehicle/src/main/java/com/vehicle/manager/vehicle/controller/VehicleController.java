package com.vehicle.manager.vehicle.controller;

import com.vehicle.manager.commons.dto.FilteringAndPagingDTO;
import com.vehicle.manager.commons.dto.model.VehicleDTO;
import com.vehicle.manager.vehicle.mapper.VehicleMapper;
import com.vehicle.manager.vehicle.service.VehicleService;
import com.vehicle.manager.vehicle.validator.ImageValidation;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;

    @PostMapping("/search")
    @Deprecated
    public Page<VehicleDTO> filterVehicles(@Valid @RequestBody FilteringAndPagingDTO filteringAndPagingDTO) {
        return vehicleService.filter(filteringAndPagingDTO)
                .map(vehicleMapper::toDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addVehicle(@NonNull @RequestPart VehicleDTO vehicleDTO, @NonNull @RequestPart @ImageValidation @Valid List<MultipartFile> images) {
        vehicleService.create(vehicleMapper.toEntity(vehicleDTO), images);
    }

    @PreAuthorize("isAuthenticated() && (hasAuthority('SCOPE_ADMIN') || @securityService.hasAccessToVehicle(#id))")
    @PutMapping("/{id}")
    public VehicleDTO updateVehicle(@PathVariable UUID id, @RequestBody VehicleDTO vehicleDTO) {
        return vehicleMapper.toDTO(vehicleService.update(vehicleMapper.toEntity(vehicleDTO), id));
    }

    @PreAuthorize("isAuthenticated() && (hasAuthority('SCOPE_ADMIN') || @securityService.hasAccessToVehicle(#id))")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(@PathVariable UUID id) {
        vehicleService.delete(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-yours")
    @Deprecated
    public Page<VehicleDTO> getAllVehiclesCreatedByYou(@RequestParam int pageNumber, @RequestParam  int pageSize) {
        return vehicleService.getAllVehiclesCreatedByYou(pageNumber, pageSize)
                .map(vehicleMapper::toDTO);
    }
}