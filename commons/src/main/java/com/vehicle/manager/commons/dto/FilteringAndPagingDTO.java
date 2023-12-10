package com.vehicle.manager.commons.dto;

import com.vehicle.manager.commons.enumeration.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilteringAndPagingDTO {
    private Integer minYear;
    private Integer maxYear;
    private Double maxAcceleration;
    private List<EuroCarSegment> euroCarSegments;
    private Double minLoadCapacity;
    private List<Drive> drives;
    private Integer minHp;
    private List<Transmission> transmissions;
    private List<EnergySource> energySources;
    private Integer minDoors;
    private Integer minSeats;
    private List<BodyType> bodyTypes;
    private List<AirConditioning> airCon;
    private List<Headlights> headlights;
    private List<Control> controls;
    private Double minAdultOccupant;
    private Double minChildOccupant;
    private Double minVulnerableRoadUsers;
    private Double minSafetyAssist;
    private List<SortBy> sortByValues;
    @NotNull
    private Integer page;
    @NotNull
    private Integer size;
}
