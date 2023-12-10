package com.vehicle.manager.vehicle.service

import com.vehicle.manager.commons.enumeration.BodyType
import com.vehicle.manager.vehicle.dao.AdditionalEquipment
import com.vehicle.manager.vehicle.dao.Body
import com.vehicle.manager.vehicle.repository.BodyRepository

import spock.lang.Specification

class BodyServiceSpec extends Specification {

    def bodyRepository = Mock(BodyRepository)
    def additionalEquipmentService = Mock(AdditionalEquipmentService)
    def bodyService = new BodyService(bodyRepository, additionalEquipmentService)

    def "updateBody_ValidBodyData_Success"() {
        given:
        def body = Mock(Body)
        def id = UUID.randomUUID()
        def bodyDb = Mock(Body)
        def additionalEquipment = Mock(AdditionalEquipment)
        def numberOfDoors = 5
        def seats = 4
        def bodyType = BodyType.Sedan

        when:
        def result = bodyService.updateBody(body)

        then:
        1 * body.getId() >> id
        1 * bodyRepository.getReferenceById(id) >> bodyDb
        1 * body.getNumberOfDoors() >> numberOfDoors
        1 * bodyDb.setNumberOfDoors(numberOfDoors)
        1 * body.getSeats() >> seats
        1 * bodyDb.setSeats(seats)
        1 * body.getBodyType() >> bodyType
        1 * bodyDb.setBodyType(bodyType)
        1 * body.getAdditionalEquipment() >> additionalEquipment
        1 * additionalEquipmentService.updateAdditionalEquipment(additionalEquipment) >> additionalEquipment
        1 * bodyDb.setAdditionalEquipment(additionalEquipment)
        0 * _
        result == bodyDb
    }
}
