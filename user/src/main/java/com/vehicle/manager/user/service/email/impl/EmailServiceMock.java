package com.vehicle.manager.user.service.email.impl;

import com.vehicle.manager.user.service.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@ConditionalOnProperty(prefix = "notification", name = "send-email", havingValue = "false")
@Slf4j
@Service
public class EmailServiceMock implements EmailService {
    @Override
    public void sendMail(String templateName, Map<String, Object> variables, String... emails) {
        log.info("Sending email to: {} with Variables: {}", Arrays.toString(emails), variables);
    }
}
