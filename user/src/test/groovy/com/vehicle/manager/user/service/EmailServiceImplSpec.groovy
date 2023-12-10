package com.vehicle.manager.user.service

import com.vehicle.manager.user.dao.Template
import com.vehicle.manager.user.service.email.impl.EmailServiceImpl
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessagePreparator
import org.thymeleaf.ITemplateEngine
import org.thymeleaf.context.Context
import spock.lang.Specification

class EmailServiceImplSpec extends Specification {
    def javaMailSender = Mock(JavaMailSender)
    def templateService = Mock(TemplateService)
    def iTemplateEngine = Mock(ITemplateEngine)
    def emailService = new EmailServiceImpl(javaMailSender, templateService, iTemplateEngine)

    def "sendMail_ValidTemplateNameVariablesAndEmails_Success"() {
        given:
        def templateName = "testTemplate"
        def variables = [key1: "value1", key2: "value2"]
        def emails = ["test1@example.com", "test2@example.com", "test3@example.com", "test4@example.com"]
        def template = Mock(Template)

        when:
        emailService.sendMail(templateName, variables, *emails)

        then:
        1 * templateService.getByName(templateName) >> template
        1 * template.getBody() >> "testBody"
        1 * iTemplateEngine.process("testBody", _ as Context) >> {
            it[1].getVariable("key1") == "value1"
            it[1].getVariable("key2") == "value2"
            it[1].locale == Locale.ENGLISH
            return "Processed Body"
        }
        1 * javaMailSender.send(_ as MimeMessagePreparator)
        0 * _
    }
}
