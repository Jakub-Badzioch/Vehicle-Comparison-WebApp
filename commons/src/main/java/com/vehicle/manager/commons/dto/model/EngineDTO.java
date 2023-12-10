package com.vehicle.manager.commons.dto.model;

import com.vehicle.manager.commons.enumeration.EnergySource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineDTO {
    private UUID id;
    private Double engineDisplacementInCubicCentimeters;
    private Integer hp;
    private Double engineTorqueInNm;
    private EnergySource energySource;
}
