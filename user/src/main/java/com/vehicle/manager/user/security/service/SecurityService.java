package com.vehicle.manager.user.security.service;


import com.vehicle.manager.commons.dto.JwtTokenDTO;
import com.vehicle.manager.commons.enumeration.Type;
import com.vehicle.manager.user.dao.Token;
import com.vehicle.manager.user.service.TokenService;
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
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final TokenService tokenService;

    public JwtTokenDTO login(String email, String password) {
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

    public void logout(String tokenJwtValue) {
        Token token = Token.builder()
                .expirationDate(LocalDateTime.now().plusDays(1))
                .value(tokenJwtValue)
                .type(Type.JWT)
                .build();
        tokenService.save(token);
    }
}