package com.vehicle.manager.user.controller;

import com.vehicle.manager.commons.dto.CredentialsDTO;
import com.vehicle.manager.commons.dto.JwtTokenDTO;
import com.vehicle.manager.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
    private final LoginService loginService;

    @PostMapping
    public JwtTokenDTO login(@RequestBody CredentialsDTO credentialsDTO) {
        return loginService.logIn(credentialsDTO.getEmail(), credentialsDTO.getPassword());
    }
}
