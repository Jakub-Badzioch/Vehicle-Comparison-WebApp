package com.vehicle.manager.vehicle.dao;

import com.vehicle.manager.commons.enumeration.BodyType;
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
public class Body {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Integer numberOfDoors;
    private Integer seats;
    @Enumerated(EnumType.STRING)
    private BodyType bodyType;
    @OneToOne(cascade=CascadeType.ALL)
    private AdditionalEquipment additionalEquipment;
}
