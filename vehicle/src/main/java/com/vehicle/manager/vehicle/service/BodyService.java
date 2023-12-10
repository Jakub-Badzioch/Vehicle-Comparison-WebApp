package com.vehicle.manager.vehicle.service;

import com.vehicle.manager.vehicle.dao.Body;
import com.vehicle.manager.vehicle.repository.BodyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BodyService {

    private final BodyRepository bodyRepository;
    private final AdditionalEquipmentService additionalEquipmentService;
    @Transactional(propagation = Propagation.MANDATORY)
    public Body updateBody(Body body){
        Body bodyDb = bodyRepository.getReferenceById(body.getId());
        bodyDb.setNumberOfDoors(body.getNumberOfDoors());
        bodyDb.setSeats(body.getSeats());
        bodyDb.setBodyType(body.getBodyType());
        bodyDb.setAdditionalEquipment(additionalEquipmentService.updateAdditionalEquipment(body.getAdditionalEquipment()));
        return bodyDb;
    }
}
