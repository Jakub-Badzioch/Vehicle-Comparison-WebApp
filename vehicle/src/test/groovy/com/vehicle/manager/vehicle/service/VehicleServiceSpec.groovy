package com.vehicle.manager.vehicle.service

import com.vehicle.manager.commons.dto.FilteringAndPagingDTO
import com.vehicle.manager.commons.enumeration.*
import com.vehicle.manager.vehicle.configuration.FolderPropertiesConfig
import com.vehicle.manager.vehicle.dao.Body
import com.vehicle.manager.vehicle.dao.Engine
import com.vehicle.manager.vehicle.dao.EuroNCAP
import com.vehicle.manager.vehicle.dao.Vehicle
import com.vehicle.manager.vehicle.repository.VehicleRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

class VehicleServiceSpec extends Specification {

    var vehicleRepository = Mock(VehicleRepository)
    var folderPropertiesConfig = Mock(FolderPropertiesConfig)
    var bodyService = Mock(BodyService)
    var euroNCAPService = Mock(EuroNCAPService)
    var engineService = Mock(EngineService)
    var vehicleService = new VehicleService(vehicleRepository, folderPropertiesConfig, bodyService, euroNCAPService, engineService)

    def "filter_ValidFilteringAndPagingDTO_Success"() {
        given:
        def filteringAndPagingDTO = FilteringAndPagingDTO.builder()
                .minYear(2004)
                .maxYear(2018)
                .maxAcceleration(4.6)
                .euroCarSegments([EuroCarSegment.F, EuroCarSegment.S])
                .minLoadCapacity(800)
                .drives([Drive.AWDAutomaticallyAttached, Drive.AWDPermanent])
                .minHp(250)
                .transmissions([Transmission.automatic])
                .energySources([EnergySource.Hybrid, EnergySource.Electricity])
                .minDoors(5)
                .minSeats(2)
                .bodyTypes([BodyType.Coupé, BodyType.Sedan])
                .airCon([AirConditioning.ThreeZoneAutomatic, AirConditioning.Automatic])
                .headlights([Headlights.BiXenon, Headlights.FrontLED])
                .controls([Control.PredictiveCruise, Control.AdaptiveCruise])
                .minAdultOccupant(80)
                .minChildOccupant(78)
                .minVulnerableRoadUsers(89)
                .minSafetyAssist(90)
                .sortByValues([SortBy.YEAR_OF_PRODUCTION])
                .page(0)
                .size(10)
                .build()

        when:
        vehicleService.filter(filteringAndPagingDTO)

        then:
        1 * vehicleRepository.findAll(_ as org.springframework.data.jpa.domain.Specification, _ as Pageable) // nie darady sprawdzic wartosci build Spec.
        0 * _
    }

    def "create_ValidVehicleAndImages_Success"() {
        given:
        def vehicle = Mock(Vehicle)
        def jpg0 = Mock(MultipartFile)
        def jpg1 = Mock(MultipartFile)
        def jpg0name = "fso_caro_1"
        def jpg1name = "fso_caro_2"
        def images = [jpg0, jpg1]
        def id = UUID.randomUUID()
        def path = "D:\\Informatyka\\PROJEKTY 2023\\VehicleComparisonSitePhotos"
        def inputStream = Mock(InputStream)

        when:
        vehicleService.create(vehicle, images)

        then:
        1 * vehicleRepository.save(vehicle) >> vehicle
        1 * folderPropertiesConfig.getProduct() >> path
        1 * vehicle.getId() >> id
        1 * jpg0.getOriginalFilename() >> jpg0name
        1 * jpg0.getInputStream() >> inputStream
        1 * inputStream.transferTo(_ as OutputStream) >> 1
        1 * jpg1.getOriginalFilename() >> jpg1name
        1 * jpg1.getInputStream() >> inputStream
        1 * inputStream.transferTo(_ as OutputStream) >> 1
        1 * vehicle.setFilePath(path + "\\" + id + "\\" + jpg1name)
        0 * _
    }

    def "create_FailedImageUpload_ThrowsIOException"() {
        given:
        def vehicle = Mock(Vehicle)
        def jpg0 = Mock(MultipartFile)
        def jpg1 = Mock(MultipartFile)
        def jpg0name = "fso_caro_1"
        def images = [jpg0, jpg1]
        def id = UUID.randomUUID()
        def path = "D:\\Informatyka\\PROJEKTY 2023\\VehicleComparisonSitePhotos"

        when:
        vehicleService.create(vehicle, images)

        then:
        1 * vehicleRepository.save(vehicle) >> vehicle
        1 * folderPropertiesConfig.getProduct() >> path
        1 * vehicle.getId() >> id
        1 * jpg0.getOriginalFilename() >> jpg0name
        1 * jpg0.getInputStream() >> {
            throw new IOException()
        }
        0 * _
        thrown IOException
    }

    def "update_ValidVehicle_Success"() {
        given:
        def vehicle = Mock(Vehicle)
        def id = UUID.randomUUID()
        def vehicleDB = Mock(Vehicle)
        def brand = Brand.Nissan
        def model = "Navara"
        def generation = 3
        def acceleration = 11.7
        def yearOfProduction = 2023
        def seats = 4
        def euroCarSegment = EuroCarSegment.D
        def deadWeightInKg = 1.628
        def maxWeightInKg = 2.298
        def drive = Drive.AWDManuallyAttached
        def transmission = Transmission.manual
        def engines = [Mock(Engine)]
        def body = Mock(Body)
        def euroNCAP = Mock(EuroNCAP)

        when:
        def result = vehicleService.update(vehicle, id)

        then:
        1 * vehicleRepository.getReferenceById(id) >> vehicleDB
        1 * vehicle.getBrand() >> brand
        1 * vehicleDB.setBrand(brand)
        1 * vehicle.getModel() >> model
        1 * vehicleDB.setModel(model)
        1 * vehicle.getYearOfProduction() >> yearOfProduction
        1 * vehicleDB.setYearOfProduction(yearOfProduction)
        1 * vehicle.getGeneration() >> generation
        1 * vehicleDB.setGeneration(generation)
        1 * vehicle.getAcceleration() >> acceleration
        1 * vehicleDB.setAcceleration(acceleration)
        1 * vehicle.getEuroCarSegment() >> euroCarSegment
        1 * vehicleDB.setEuroCarSegment(euroCarSegment)
        1 * vehicle.getKerbWeight() >> deadWeightInKg
        1 * vehicleDB.setKerbWeight(deadWeightInKg)
        1 * vehicle.getGrossWeight() >> maxWeightInKg
        1 * vehicleDB.setGrossWeight(maxWeightInKg)
        1 * vehicle.getDrive() >> drive
        1 * vehicleDB.setDrive(drive)
        1 * vehicle.getTransmission() >> transmission
        1 * vehicleDB.setTransmission(transmission)
        1 * vehicle.getEngines() >> engines
        1 * engineService.updateEngines(engines) >> engines
        1 * vehicleDB.setEngines(engines)
        1 * vehicle.getBody() >> body
        1 * bodyService.updateBody(body) >> body
        1 * vehicleDB.setBody(body)
        1 * vehicle.getEuroNCAP() >> euroNCAP
        1 * euroNCAPService.updateEuroNCAP(euroNCAP) >> euroNCAP
        1 * vehicleDB.setEuroNCAP(euroNCAP)
        0 * _
        result == vehicleDB
    }

    def "delete_ValidVehicle_Success"() {
        given:
        def id = UUID.randomUUID()
        def vehicle = Mock(Vehicle)
        def e1 = Mock(Engine)
        def e2 = Mock(Engine)
        def engines = [e1, e2]

        when:
        vehicleService.delete(id)

        then:
        1 * vehicleRepository.getReferenceById(id) >> vehicle
        1 * vehicle.getEngines() >> engines
        1 * engineService.deleteEngines(engines)
        1 * vehicleRepository.deleteById(id)
        0 * _
    }

    def "getAllVehiclesCreatedByYou_ValidInput_Success"() {
        given:
        def securityContext = Mock(SecurityContext)
        def authentication = Mock(Authentication)
        SecurityContextHolder.setContext(securityContext)
        def email = "test@example"

        when:
        vehicleService.getAllVehiclesCreatedByYou(0, 10)

        then:
        1 * securityContext.getAuthentication() >> authentication
        1 * authentication.getName() >> email
        1 * vehicleRepository.findByCreatedBy(email, PageRequest.of(0, 10))
        0 * _
    }
}
