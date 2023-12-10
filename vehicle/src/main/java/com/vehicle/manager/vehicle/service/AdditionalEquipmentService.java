package com.vehicle.manager.vehicle.service;

import com.vehicle.manager.vehicle.dao.AdditionalEquipment;
import com.vehicle.manager.vehicle.repository.AdditionalEquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdditionalEquipmentService {

    private final AdditionalEquipmentRepository additionalEquipmentRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public AdditionalEquipment updateAdditionalEquipment(AdditionalEquipment additionalEquipment) {
        AdditionalEquipment additionalEquipmentDb = additionalEquipmentRepository.getReferenceById(additionalEquipment.getId());
        additionalEquipmentDb.setAirConditioning(additionalEquipment.getAirConditioning());
        additionalEquipmentDb.setHeadlights(additionalEquipment.getHeadlights());
        additionalEquipmentDb.setControl(additionalEquipment.getControl());
        return additionalEquipmentDb;
    }
}
