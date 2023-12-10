package com.vehicle.manager.commons.dto.model;

import com.vehicle.manager.commons.enumeration.AirConditioning;
import com.vehicle.manager.commons.enumeration.Control;
import com.vehicle.manager.commons.enumeration.Headlights;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalEquipmentDTO {
    private UUID id;
    private AirConditioning airConditioning;
    private Headlights headlights;
    private Control control;
}
