package com.vehicle.manager.commons.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EuroNCAPDTO {
    private UUID id;
    private Double adultOccupantPercentage;
    private Double childOccupantPercentage;
    private Double vulnerableRoadUsersPercentage;
    private Double safetyAssistPercentage;
}
