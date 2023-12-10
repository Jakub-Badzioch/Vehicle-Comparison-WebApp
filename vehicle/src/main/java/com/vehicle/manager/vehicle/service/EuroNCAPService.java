package com.vehicle.manager.vehicle.service;

import com.vehicle.manager.vehicle.dao.EuroNCAP;
import com.vehicle.manager.vehicle.repository.EuroNCAPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EuroNCAPService {

    private final EuroNCAPRepository euroNCAPRepository;
    @Transactional(propagation = Propagation.MANDATORY)
    public EuroNCAP updateEuroNCAP(EuroNCAP euroNCAP) {
        EuroNCAP euroNCAPDB = euroNCAPRepository.getReferenceById(euroNCAP.getId());
        euroNCAPDB.setAdultOccupantPercentage(euroNCAP.getAdultOccupantPercentage());
        euroNCAPDB.setChildOccupantPercentage(euroNCAP.getChildOccupantPercentage());
        euroNCAPDB.setVulnerableRoadUsersPercentage(euroNCAP.getVulnerableRoadUsersPercentage());
        euroNCAPDB.setSafetyAssistPercentage(euroNCAP.getSafetyAssistPercentage());
        return euroNCAPDB;
    }
}
