package com.vehicle.manager.user.service

import com.vehicle.manager.user.dao.Template
import com.vehicle.manager.user.repository.TemplateRepository
import jakarta.persistence.EntityNotFoundException
import spock.lang.Specification

class TemplateServiceSpec extends Specification {
    TemplateRepository templateRepository = Mock(TemplateRepository)
    TemplateService templateService = new TemplateService(templateRepository)

    def "getByName_ValidName_ReturnsTemplate"() {
        given:
        def templateName = "validTemplate"
        def template = Mock(Template)
        def optionalTemplate = Optional.of(template)

        when:
        def result = templateService.getByName(templateName)

        then:
        1 * templateRepository.findByName(templateName) >> optionalTemplate
        0 * _
        result == template
    }

    def "getByName_InvalidName_ThrowsEntityNotFoundException"() {
        given:
        def templateName = "invalidTemplate"

        when:
        templateService.getByName(templateName)

        then:
        1 * templateRepository.findByName(templateName) >> Optional.empty()
        0 * _
        def e = thrown EntityNotFoundException
        e.message == templateName
    }
}
