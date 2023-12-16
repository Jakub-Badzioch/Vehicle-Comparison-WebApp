package com.vehicle.manager.vehicle.dao;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.vehicle.manager.commons.enumeration.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle implements IdentifiedDataSerializable {
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
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Body body;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private EuroNCAP euroNCAP;

    @Override
    public int getFactoryId() {
        return 1;
    }

    @Override
    public int getClassId() {
        return 1;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeString(id.toString());
        objectDataOutput.writeString(createdBy);
        objectDataOutput.writeString(filePath);
        objectDataOutput.writeString(brand.toString());
        objectDataOutput.writeString(model);
        objectDataOutput.writeInt(yearOfProduction);
        objectDataOutput.writeInt(generation);
        objectDataOutput.writeDouble(acceleration);
        objectDataOutput.writeString(euroCarSegment.toString());
        objectDataOutput.writeDouble(kerbWeight);
        objectDataOutput.writeDouble(grossWeight);
        objectDataOutput.writeString(drive.toString());
        objectDataOutput.writeString(transmission.toString());

        objectDataOutput.writeInt(engines != null ? engines.size() : 0);
        for (Engine engine : engines) {
            objectDataOutput.writeString(engine.getId().toString());
            objectDataOutput.writeDouble(engine.getEngineDisplacementInCubicCentimeters());
            objectDataOutput.writeInt(engine.getHp());
            objectDataOutput.writeDouble(engine.getEngineTorqueInNm());
            objectDataOutput.writeString(engine.getEnergySource().toString());
        }

        objectDataOutput.writeString(body.getAdditionalEquipment().getId().toString());
        objectDataOutput.writeString(body.getAdditionalEquipment().getAirConditioning().toString());
        objectDataOutput.writeString(body.getAdditionalEquipment().getHeadlights().toString());
        objectDataOutput.writeString(body.getAdditionalEquipment().getControl().toString());

        objectDataOutput.writeString(body.getId().toString());
        objectDataOutput.writeInt(body.getNumberOfDoors());
        objectDataOutput.writeInt(body.getSeats());
        objectDataOutput.writeString(body.getBodyType().toString());

        objectDataOutput.writeString(euroNCAP.getId().toString());
        objectDataOutput.writeDouble(euroNCAP.getAdultOccupantPercentage());
        objectDataOutput.writeDouble(euroNCAP.getChildOccupantPercentage());
        objectDataOutput.writeDouble(euroNCAP.getVulnerableRoadUsersPercentage());
        objectDataOutput.writeDouble(euroNCAP.getSafetyAssistPercentage());
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        id = UUID.fromString(Objects.requireNonNull(objectDataInput.readString()));
        createdBy = objectDataInput.readString();
        filePath = objectDataInput.readString();
        brand = Brand.valueOf(objectDataInput.readString());
        model = objectDataInput.readString();
        yearOfProduction = objectDataInput.readInt();
        generation = objectDataInput.readInt();
        acceleration = objectDataInput.readDouble();
        acceleration = objectDataInput.readDouble();
        euroCarSegment = EuroCarSegment.valueOf(objectDataInput.readString());
        kerbWeight = objectDataInput.readDouble();
        grossWeight = objectDataInput.readDouble();
        drive = Drive.valueOf(objectDataInput.readString());
        transmission = Transmission.valueOf(objectDataInput.readString());

        for (int i = 0; i < objectDataInput.readInt(); i++) {
            Engine build = Engine.builder()
                    .id(UUID.fromString(Objects.requireNonNull(objectDataInput.readString())))
                    .engineDisplacementInCubicCentimeters(objectDataInput.readDouble())
                    .hp(objectDataInput.readInt())
                    .engineTorqueInNm(objectDataInput.readDouble())
                    .energySource(EnergySource.valueOf(objectDataInput.readString()))
                    .build();
            engines.add(build);
        }

        AdditionalEquipment additionalEquipmentBuild = AdditionalEquipment.builder()
                .id(UUID.fromString(Objects.requireNonNull(objectDataInput.readString())))
                .airConditioning(AirConditioning.valueOf(objectDataInput.readString()))
                .headlights(Headlights.valueOf(objectDataInput.readString()))
                .control(Control.valueOf(objectDataInput.readString()))
                .build();

        body = Body.builder()
                .id(UUID.fromString(Objects.requireNonNull(objectDataInput.readString())))
                .numberOfDoors(objectDataInput.readInt())
                .seats(objectDataInput.readInt())
                .bodyType(BodyType.valueOf(objectDataInput.readString()))
                .additionalEquipment(additionalEquipmentBuild)
                .build();

        euroNCAP = EuroNCAP.builder()
                .id(UUID.fromString(Objects.requireNonNull(objectDataInput.readString())))
                .adultOccupantPercentage(objectDataInput.readDouble())
                .childOccupantPercentage(objectDataInput.readDouble())
                .vulnerableRoadUsersPercentage(objectDataInput.readDouble())
                .safetyAssistPercentage(objectDataInput.readDouble())
                .build();
    }
}