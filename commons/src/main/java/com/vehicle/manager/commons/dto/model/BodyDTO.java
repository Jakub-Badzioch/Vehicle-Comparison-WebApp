package com.vehicle.manager.commons.dto.model;

import com.vehicle.manager.commons.enumeration.BodyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyDTO {
    private UUID id;
    private Integer numberOfDoors;
    private Integer seats;
    private BodyType bodyType;
    private AdditionalEquipmentDTO additionalEquipment;
}
