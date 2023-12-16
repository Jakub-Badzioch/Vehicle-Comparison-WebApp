package com.vehicle.manager.vehicle.service;

import com.vehicle.manager.commons.dto.FilteringAndPagingDTO;
import com.vehicle.manager.vehicle.builder.VehiclePredicateBuilder;
import com.vehicle.manager.vehicle.configuration.FolderPropertiesConfig;
import com.vehicle.manager.vehicle.dao.Vehicle;
import com.vehicle.manager.vehicle.repository.VehicleRepository;
import com.vehicle.manager.vehicle.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final FolderPropertiesConfig folderPropertiesConfig;
    private final BodyService bodyService;
    private final EuroNCAPService euroNCAPService;
    private final EngineService engineService;

    public Page<Vehicle> filter(FilteringAndPagingDTO filteringAndPagingDTO) {
        Pageable pageable;
        if (filteringAndPagingDTO.getSortByValues() != null && !filteringAndPagingDTO.getSortByValues().isEmpty()) {
            List<Sort.Order> orders = filteringAndPagingDTO.getSortByValues().stream()
                    .map(sortBy -> switch (sortBy) {
                                case YEAR_OF_PRODUCTION -> Sort.Order.desc("yearOfProduction");
                                case GENERATION -> Sort.Order.desc("generation");
                                case ACCELERATION -> Sort.Order.desc("acceleration");
                                case SEATS -> Sort.Order.desc("seats");
                            }
                    )
                    .toList();
            pageable = PageRequest.of(filteringAndPagingDTO.getPage(), filteringAndPagingDTO.getSize(), Sort.by(orders));
        } else {
            pageable = PageRequest.of(filteringAndPagingDTO.getPage(), filteringAndPagingDTO.getSize());
        }
        Specification<Vehicle> spec = buildVehicleSpecification(filteringAndPagingDTO);
        return vehicleRepository.findAll(spec, pageable);
    }

    @Cacheable(cacheNames = "vehicle", key = "#id")
    public Vehicle getById(UUID id) {
        return vehicleRepository.getReferenceById(id);
    }

    @SneakyThrows
    @CachePut(cacheNames = "vehicle", key = "#result.id")
    @Transactional
    public void create(Vehicle vehicle, List<MultipartFile> images) {
        vehicleRepository.save(vehicle);
        final Path path = Paths.get(folderPropertiesConfig.getProduct(), vehicle.getId().toString());
        FileUtils.createDirectoryIfNotExists(path);
        Path completeFilePath = null;
        for (MultipartFile image : images) {
            completeFilePath = Paths.get(path.toString(), image.getOriginalFilename());
            FileUtils.saveInputStreamAsFile(image.getInputStream(), completeFilePath);
        }
        assert completeFilePath != null;
        vehicle.setFilePath(completeFilePath.toString());
    }

    @Transactional
    @CachePut(cacheNames = "vehicle", key = "#result.id")
    public Vehicle update(Vehicle vehicle, UUID id) {
        final Vehicle vehicleDb = vehicleRepository.getReferenceById(id);
        vehicleDb.setBrand(vehicle.getBrand());
        vehicleDb.setModel(vehicle.getModel());
        vehicleDb.setYearOfProduction(vehicle.getYearOfProduction());
        vehicleDb.setGeneration(vehicle.getGeneration());
        vehicleDb.setAcceleration(vehicle.getAcceleration());
        vehicleDb.setEuroCarSegment(vehicle.getEuroCarSegment());
        vehicleDb.setKerbWeight(vehicle.getKerbWeight());
        vehicleDb.setGrossWeight(vehicle.getGrossWeight());
        vehicleDb.setDrive(vehicle.getDrive());
        vehicleDb.setTransmission(vehicle.getTransmission());
        vehicleDb.setEngines(engineService.updateEngines(vehicle.getEngines()));
        vehicleDb.setBody(bodyService.updateBody(vehicle.getBody()));
        vehicleDb.setEuroNCAP(euroNCAPService.updateEuroNCAP(vehicle.getEuroNCAP()));
        return vehicleDb;
    }

    @CacheEvict(cacheNames = "vehicle", key = "#id")
    @Transactional
    public void delete(UUID id) {
        Vehicle vehicle = vehicleRepository.getReferenceById(id);
        engineService.deleteEngines(vehicle.getEngines());
        vehicleRepository.deleteById(id);
    }

    public Page<Vehicle> getAllVehiclesCreatedByYou(int pageNumber, int pageSize) {
        return vehicleRepository.findByCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName(), PageRequest.of(pageNumber, pageSize));
    }

    private Specification<Vehicle> buildVehicleSpecification(FilteringAndPagingDTO dto) {
        return (root, criteriaQuery, criteriaBuilder) -> VehiclePredicateBuilder.builder(criteriaBuilder, root, criteriaQuery)
                .minYear(dto.getMinYear())
                .maxYear(dto.getMaxYear())
                .maxAcceleration(dto.getMaxAcceleration())
                .euroCarSegments(dto.getEuroCarSegments())
                .minLoadCapacity(dto.getMinLoadCapacity())
                .drives(dto.getDrives())
                .minHp(dto.getMinHp())
                .transmissions(dto.getTransmissions())
                .energySources(dto.getEnergySources())
                .minDoors(dto.getMinDoors())
                .minSeats(dto.getMinSeats())
                .bodyTypes(dto.getBodyTypes())
                .airCon(dto.getAirCon())
                .headlights(dto.getHeadlights())
                .controls(dto.getControls())
                .minAdultOccupant(dto.getMinAdultOccupant())
                .minChildOccupant(dto.getMinChildOccupant())
                .minVulnerableRoadUsers(dto.getMinVulnerableRoadUsers())
                .minSafetyAssist(dto.getMinSafetyAssist())
                .build();
    }
}