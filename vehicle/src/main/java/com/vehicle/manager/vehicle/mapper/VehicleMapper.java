package com.vehicle.manager.vehicle.mapper;

import com.vehicle.manager.commons.dto.model.VehicleDTO;
import com.vehicle.manager.vehicle.dao.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    @Mapping(target = "id", ignore = true)
    Vehicle toEntity(VehicleDTO vehicleDTO);

    VehicleDTO toDTO(Vehicle vehicle);
    @Mapping(target = "engines", ignore = true)
    @Mapping(target = "body", ignore = true)
    @Mapping(target = "euroNCAP", ignore = true)
    VehicleDTO toThinDTO(Vehicle vehicle);
}
