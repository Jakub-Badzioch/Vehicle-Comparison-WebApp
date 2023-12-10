package com.vehicle.manager.vehicle.dao;

import com.vehicle.manager.commons.enumeration.EnergySource;
import com.vehicle.manager.commons.enumeration.Transmission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Double engineDisplacementInCubicCentimeters;
    private Integer hp;
    private Double engineTorqueInNm;
    @Enumerated(EnumType.STRING)
    private EnergySource energySource;
}
