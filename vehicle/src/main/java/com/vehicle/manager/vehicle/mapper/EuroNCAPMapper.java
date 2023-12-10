package com.vehicle.manager.vehicle.mapper;

import com.vehicle.manager.commons.dto.model.VehicleDTO;
import com.vehicle.manager.vehicle.dao.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EuroNCAPMapper {
    @Mapping(target = "id", ignore = true)
    Vehicle toEntity(VehicleDTO vehicleDTO);

    VehicleDTO toDTO(Vehicle vehicle);
}
