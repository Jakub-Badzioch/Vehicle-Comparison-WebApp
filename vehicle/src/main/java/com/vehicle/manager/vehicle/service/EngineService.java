package com.vehicle.manager.vehicle.service;

import com.vehicle.manager.vehicle.dao.Engine;
import com.vehicle.manager.vehicle.repository.EngineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EngineService {

    private final EngineRepository engineRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public List<Engine> updateEngines(List<Engine> engines) {
        return engines.stream()
                .map(this::updateEngine)
                .toList();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Engine updateEngine(Engine engine) {
        Engine engineDB = engineRepository.getReferenceById(engine.getId());
        engineDB.setEngineDisplacementInCubicCentimeters(engine.getEngineDisplacementInCubicCentimeters());
        engineDB.setHp(engine.getHp());
        engineDB.setEngineTorqueInNm(engine.getEngineTorqueInNm());
        engineDB.setEnergySource(engine.getEnergySource());
        return engineDB;
    }

    public void deleteEngines(List<Engine> engines) {
        engineRepository.deleteByIdIn(engines.stream()
                .map(Engine::getId)
                .toList());
    }
}
