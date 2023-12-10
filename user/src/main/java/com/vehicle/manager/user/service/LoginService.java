package com.vehicle.manager.user.service;


import com.vehicle.manager.commons.dto.JwtTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    public JwtTokenDTO logIn(String email, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .expiresAt(Instant.now().plus(Duration.ofDays(1)))
                .claim("scope", "USER")
                .build();
        final String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new JwtTokenDTO(tokenValue);
    }
}
