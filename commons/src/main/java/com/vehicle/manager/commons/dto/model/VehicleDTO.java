package com.vehicle.manager.commons.dto.model;

import com.vehicle.manager.commons.enumeration.Brand;
import com.vehicle.manager.commons.enumeration.Drive;
import com.vehicle.manager.commons.enumeration.EuroCarSegment;
import com.vehicle.manager.commons.enumeration.Transmission;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private UUID id;
    private String createdBy;
    private String filePath;
    private Brand brand;
    private String model;
    private Integer yearOfProduction;
    private Integer generation;
    private Double acceleration;
    private EuroCarSegment euroCarSegment;
    private Double kerbWeight;
    private Double grossWeight;
    private Drive drive;
    private Transmission transmission;
    @Singular
    private List<EngineDTO> engines;
    private BodyDTO body;
    private EuroNCAPDTO euroNCAP;
}
