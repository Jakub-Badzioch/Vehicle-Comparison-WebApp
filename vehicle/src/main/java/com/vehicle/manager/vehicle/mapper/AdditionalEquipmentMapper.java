package com.vehicle.manager.vehicle.mapper;

import com.vehicle.manager.commons.dto.model.AdditionalEquipmentDTO;
import com.vehicle.manager.vehicle.dao.AdditionalEquipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdditionalEquipmentMapper {
    @Mapping(target = "id", ignore = true)
    AdditionalEquipment toEntity(AdditionalEquipmentDTO additionalEquipmentDTO);

    AdditionalEquipmentDTO toDTO(AdditionalEquipment additionalEquipment);
}
