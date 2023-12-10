package com.vehicle.manager.vehicle.service


import com.vehicle.manager.commons.enumeration.EnergySource
import com.vehicle.manager.vehicle.dao.Engine
import com.vehicle.manager.vehicle.repository.EngineRepository
import spock.lang.Specification

class EngineServiceSpec extends Specification {

    EngineRepository engineRepository = Mock(EngineRepository)
    EngineService engineService = new EngineService(engineRepository)


    def "updateEngines_ValidEngineData_Success"() {
        given:
        def engine0 = Mock(Engine)
        def engine1 = Mock(Engine)
        def engines = [engine0, engine1]
        def id0 = UUID.randomUUID()
        def id1 = UUID.randomUUID()
        def engine0Db = Mock(Engine)
        def engine1Db = Mock(Engine)
        def engineDisplacementInCubicCentimeters = 1199.9
        def hp = 98
        def engineTorqueInNm = 2399.23
        def energySource = EnergySource.Gasoline

        when:
        engineService.updateEngines(engines)

        then:
        1 * engine0.getId() >> id0
        1 * engine1.getId() >> id1
        1 * engineRepository.getReferenceById(id0) >> engine0Db
        1 * engine0.getEngineDisplacementInCubicCentimeters() >> engineDisplacementInCubicCentimeters
        1 * engine0Db.setEngineDisplacementInCubicCentimeters(engineDisplacementInCubicCentimeters)
        1 * engine0.getHp() >> hp
        1 * engine0Db.setHp(hp)
        1 * engine0.getEngineTorqueInNm() >> engineTorqueInNm
        1 * engine0Db.setEngineTorqueInNm(engineTorqueInNm)
        1 * engine0.getEnergySource() >> energySource
        1 * engine0Db.setEnergySource(energySource)
        1 * engineRepository.getReferenceById(id1) >> engine1Db
        1 * engine1.getEngineDisplacementInCubicCentimeters() >> engineDisplacementInCubicCentimeters
        1 * engine1Db.setEngineDisplacementInCubicCentimeters(engineDisplacementInCubicCentimeters)
        1 * engine1.getHp() >> hp
        1 * engine1Db.setHp(hp)
        1 * engine1.getEngineTorqueInNm() >> engineTorqueInNm
        1 * engine1Db.setEngineTorqueInNm(engineTorqueInNm)
        1 * engine1.getEnergySource() >> energySource
        1 * engine1Db.setEnergySource(energySource)
        0 * _
    }

    def "updateEngine_ValidEngineData_Success"() {
        given:
        def engine = Mock(Engine)
        def id = UUID.randomUUID()
        def engineDb = Mock(Engine)
        def engineDisplacementInCubicCentimeters = 1199.9
        def hp = 98
        def engineTorqueInNm = 2399.23
        def energySource = EnergySource.Gasoline

        when:
        def result = engineService.updateEngine(engine)

        then:
        1 * engine.getId() >> id
        1 * engineRepository.getReferenceById(id) >> engineDb
        1 * engine.getEngineDisplacementInCubicCentimeters() >> engineDisplacementInCubicCentimeters
        1 * engineDb.setEngineDisplacementInCubicCentimeters(engineDisplacementInCubicCentimeters)
        1 * engine.getHp() >> hp
        1 * engineDb.setHp(hp)
        1 * engine.getEngineTorqueInNm() >> engineTorqueInNm
        1 * engineDb.setEngineTorqueInNm(engineTorqueInNm)
        1 * engine.getEnergySource() >> energySource
        1 * engineDb.setEnergySource(energySource)
        0 * _
        result == engineDb
    }

    def "deleteEngines_ValidEnginesData_Success"() {
        given:
        def engine0 = Mock(Engine)
        def engine1 = Mock(Engine)
        def engines = [engine0, engine1]
        def id0 = UUID.randomUUID()
        def id1 = UUID.randomUUID()
        def ids = [id0, id1]

        when:
        engineService.deleteEngines(engines)

        then:
        1 * engine0.getId() >> id0
        1 * engine1.getId() >> id1
        1 * engineRepository.deleteByIdIn(ids)
        0 * _
    }
}
