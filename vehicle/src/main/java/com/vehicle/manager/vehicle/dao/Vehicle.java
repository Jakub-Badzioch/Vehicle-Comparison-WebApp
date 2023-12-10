package com.vehicle.manager.vehicle.dao;

import com.vehicle.manager.commons.enumeration.Brand;
import com.vehicle.manager.commons.enumeration.Drive;
import com.vehicle.manager.commons.enumeration.EuroCarSegment;
import com.vehicle.manager.commons.enumeration.Transmission;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @CreatedBy
    private String createdBy;
    private String filePath;
    @Enumerated(EnumType.STRING)
    private Brand brand;
    private String model;
    private Integer yearOfProduction;
    private Integer generation;
    private Double acceleration;
    @Enumerated(EnumType.STRING)
    private EuroCarSegment euroCarSegment;
    private Double kerbWeight;
    private Double grossWeight;
    @Enumerated(EnumType.STRING)
    private Drive drive;
    @Enumerated(EnumType.STRING)
    private Transmission transmission;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "vehicle_engine")
    @Singular
    private List<Engine> engines;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) // orphanRemoval = true od razu usunie sie relacja jak usune vehicla po id
    private Body body;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private EuroNCAP euroNCAP;
}
