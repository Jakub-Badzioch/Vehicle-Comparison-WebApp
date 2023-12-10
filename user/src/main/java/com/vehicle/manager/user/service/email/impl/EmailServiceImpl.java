package com.vehicle.manager.user.service.email.impl;

import com.vehicle.manager.user.dao.Template;
import com.vehicle.manager.user.service.TemplateService;
import com.vehicle.manager.user.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification", name = "send-email", havingValue = "true")
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateService templateService;
    private final ITemplateEngine iTemplateEngine;

    @Async
    public void sendMail(String templateName, Map<String, Object> variables, String... emails) {
        final Template template = templateService.getByName(templateName);
        final String body = iTemplateEngine.process(template.getBody(), new Context(Locale.getDefault(), variables));
        javaMailSender.send(mimeMessage -> {
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setBcc(emails);
            helper.setSubject(template.getSubject());
            helper.setText(body, true);
        });
    }
}