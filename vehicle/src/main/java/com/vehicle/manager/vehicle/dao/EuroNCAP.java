package com.vehicle.manager.vehicle.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class EuroNCAP {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Double adultOccupantPercentage;
    private Double childOccupantPercentage;
    private Double vulnerableRoadUsersPercentage;
    private Double safetyAssistPercentage;
}
