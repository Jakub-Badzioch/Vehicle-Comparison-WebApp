package com.vehicle.manager.vehicle.mapper;

import com.vehicle.manager.commons.dto.model.EngineDTO;
import com.vehicle.manager.vehicle.dao.Engine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EngineMapper {
    @Mapping(target = "id", ignore = true)
    Engine toEntity(EngineDTO engineDTO);

    EngineDTO toDTO(Engine engine);
}
