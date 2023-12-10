package com.vehicle.manager.vehicle.service

import com.vehicle.manager.vehicle.repository.VehicleRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

class SecurityServiceSpec extends Specification {
    def vehicleRepository = Mock(VehicleRepository)
    def securityService = new SecurityService(vehicleRepository)

    def "hasAccessToVehicle_ValidInput_True"() {
        given:
        def id = UUID.randomUUID()
        def securityContext = Mock(SecurityContext)
        def authentication = Mock(Authentication)
        SecurityContextHolder.setContext(securityContext)
        def email = "test@example"

        when:
        securityService.hasAccessToVehicle(id)

        then:
        1 * securityContext.getAuthentication() >> authentication
        1 * authentication.getName() >> email
        1 * vehicleRepository.existsByIdAndCreatedBy(id, email) >> true
        0 * _
    }
}
