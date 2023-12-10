package com.vehicle.manager.vehicle.service


import com.vehicle.manager.vehicle.dao.EuroNCAP
import com.vehicle.manager.vehicle.repository.EuroNCAPRepository
import spock.lang.Specification

class EuroNCAPServiceSpec extends Specification {

    EuroNCAPRepository euroNCAPRepository = Mock(EuroNCAPRepository)
    EuroNCAPService euroNCAPService = new EuroNCAPService(euroNCAPRepository)

    def "updateEuroNCAP_ValidEuroNCAPData_Success"() {
        given:
        def euroNCAP = Mock(EuroNCAP)
        def euroNCAPDB = Mock(EuroNCAP)
        def id = UUID.randomUUID()
        def adultOccupantPercentage = 67
        def childOccupantPercentage = 77
        def vulnerableRoadUsersPercentage = 71
        def safetyAssistPercentage = 79

        when:
        def result = euroNCAPService.updateEuroNCAP(euroNCAP)

        then:
        1 * euroNCAP.getId() >> id
        1 * euroNCAPRepository.getReferenceById(id) >> euroNCAPDB
        1 * euroNCAP.getAdultOccupantPercentage() >> adultOccupantPercentage
        1 * euroNCAPDB.setAdultOccupantPercentage(adultOccupantPercentage)
        1 * euroNCAP.getChildOccupantPercentage() >> childOccupantPercentage
        1 * euroNCAPDB.setChildOccupantPercentage(childOccupantPercentage)
        1 * euroNCAP.getVulnerableRoadUsersPercentage() >> vulnerableRoadUsersPercentage
        1 * euroNCAPDB.setVulnerableRoadUsersPercentage(vulnerableRoadUsersPercentage)
        1 * euroNCAP.getSafetyAssistPercentage() >> safetyAssistPercentage
        1 * euroNCAPDB.setSafetyAssistPercentage(safetyAssistPercentage)
        0 * _
        result == euroNCAPDB
    }
}
