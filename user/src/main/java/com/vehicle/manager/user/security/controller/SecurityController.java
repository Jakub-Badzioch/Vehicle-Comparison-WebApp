package com.vehicle.manager.user.security.controller;

import com.vehicle.manager.commons.dto.CredentialsDTO;
import com.vehicle.manager.commons.dto.JwtTokenDTO;
import com.vehicle.manager.user.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/security", produces = MediaType.APPLICATION_JSON_VALUE)
public class SecurityController {
    private final SecurityService securityService;

    @PostMapping("/login")
    public JwtTokenDTO login(@RequestBody CredentialsDTO credentialsDTO) {
        return securityService.login(credentialsDTO.getEmail(), credentialsDTO.getPassword());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public void logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String tokenJwtValue) {
        securityService.logout(tokenJwtValue);
    }
}
