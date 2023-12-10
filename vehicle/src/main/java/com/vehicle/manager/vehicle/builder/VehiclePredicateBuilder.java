package com.vehicle.manager.vehicle.builder;

import com.vehicle.manager.commons.enumeration.*;
import com.vehicle.manager.vehicle.dao.Vehicle;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class VehiclePredicateBuilder {
    private final List<Predicate> predicates = new LinkedList<>();
    private final CriteriaBuilder criteriaBuilder;
    private final Root<Vehicle> root;
    private final CriteriaQuery<?> criteriaQuery;

    public static VehiclePredicateBuilder builder(CriteriaBuilder criteriaBuilderFactory, Root<Vehicle> vehicleRoot, CriteriaQuery<?> query) {
        return new VehiclePredicateBuilder(criteriaBuilderFactory, vehicleRoot, query);
    }

    public VehiclePredicateBuilder minYear(Integer minYear) {
        return minValueAtLeast(minYear, root.get("yearOfProduction"));
    }

    public VehiclePredicateBuilder maxYear(Integer maxYear) {
        return maxValueAtMost(maxYear, root.get("yearOfProduction"));
    }

    public VehiclePredicateBuilder maxAcceleration(Double accelerationSlowest) {
        return maxValueAtMost(accelerationSlowest, root.get("acceleration"));
    }

    public VehiclePredicateBuilder euroCarSegments(List<EuroCarSegment> euroCarSegments) {
        return includeListValues(euroCarSegments, root.get("euroCarSegment"));
    }

    public VehiclePredicateBuilder minLoadCapacity(Double minLoadCapacityInKg) {
        return minValueAtLeast(minLoadCapacityInKg, root.get("kerbWeight"));
    }

    public VehiclePredicateBuilder drives(List<Drive> drives) {
        return includeListValues(drives, root.get("drive"));
    }

    public VehiclePredicateBuilder minHp(Integer minHp) {
        return minValueAtLeast(minHp, root.get("engines").get("hp"));
    }

    public VehiclePredicateBuilder transmissions(List<Transmission> transmissions) {
        return includeListValues(transmissions, root.get("transmission"));
    }

    public VehiclePredicateBuilder energySources(List<EnergySource> energySources) {
        return includeListValues(energySources, root.get("engines").get("energySource"));
    }

    public VehiclePredicateBuilder minDoors(Integer minNumberOfDoors) {
        return minValueAtLeast(minNumberOfDoors, root.get("body").get("numberOfDoors"));
    }

    public VehiclePredicateBuilder minSeats(Integer minNumberOfSeats) {
        return minValueAtLeast(minNumberOfSeats, root.get("body").get("seats"));
    }

    public VehiclePredicateBuilder bodyTypes(List<BodyType> bodyTypes) {
        return includeListValues(bodyTypes, root.get("body").get("bodyType"));
    }

    public VehiclePredicateBuilder airCon(List<AirConditioning> airConditionings) {
        return includeListValues(airConditionings, root.get("body").get("additionalEquipment").get("airConditioning"));
    }

    public VehiclePredicateBuilder headlights(List<Headlights> headlights) {
        return includeListValues(headlights, root.get("body").get("additionalEquipment").get("headlights"));
    }

    public VehiclePredicateBuilder controls(List<Control> controls) {
        return includeListValues(controls, root.get("body").get("additionalEquipment").get("control"));
    }

    public VehiclePredicateBuilder minAdultOccupant(Double minAdultOccupantPercentage) {
        return minValueAtLeast(minAdultOccupantPercentage, root.get("euroNCAP").get("adultOccupantPercentage"));
    }

    public VehiclePredicateBuilder minChildOccupant(Double minChildOccupantPercentage) {
        return minValueAtLeast(minChildOccupantPercentage, root.get("euroNCAP").get("childOccupantPercentage"));
    }

    public VehiclePredicateBuilder minVulnerableRoadUsers(Double minVulnerableRoadUsersPercentage) {
        return minValueAtLeast(minVulnerableRoadUsersPercentage, root.get("euroNCAP").get("vulnerableRoadUsersPercentage"));
    }

    public VehiclePredicateBuilder minSafetyAssist(Double minSafetyAssistPercentage) {
        return minValueAtLeast(minSafetyAssistPercentage, root.get("euroNCAP").get("safetyAssistPercentage"));
    }

    public Predicate build() {
        Specification<Vehicle> transfer = (root1, query1, criteriaBuilder1) -> criteriaBuilder1.and(predicates.toArray(new Predicate[0]));
        return transfer.toPredicate(root, criteriaQuery, criteriaBuilder);
    }

    private <T> VehiclePredicateBuilder includeListValues(List<T> values, Path<Object> path) {
        if (values != null && !values.isEmpty()) {
            predicates.add(path.in(values));
        }
        return this;
    }

    private <T extends Comparable<T>> VehiclePredicateBuilder minValueAtLeast(T min, Path<T> path) {
        if (min != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, min));
        }
        return this;
    }

    private <T extends Comparable<T>> VehiclePredicateBuilder maxValueAtMost(T max, Path<T> path) {
        if (max != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(path, max));
        }
        return this;
    }
}