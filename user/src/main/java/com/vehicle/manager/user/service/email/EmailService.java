package com.vehicle.manager.user.service.email;

import java.util.Map;

public interface EmailService {
    void sendMail(String templateName, Map<String, Object> variables, String... emails);
}
