package com.vehicle.manager.vehicle.dao;

import com.vehicle.manager.commons.enumeration.AirConditioning;
import com.vehicle.manager.commons.enumeration.Control;
import com.vehicle.manager.commons.enumeration.Headlights;
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
public class AdditionalEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private AirConditioning airConditioning;
    @Enumerated(EnumType.STRING)
    private Headlights headlights;
    @Enumerated(EnumType.STRING)
    private Control control;
}
