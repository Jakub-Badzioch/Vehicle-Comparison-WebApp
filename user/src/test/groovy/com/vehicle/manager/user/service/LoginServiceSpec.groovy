package com.vehicle.manager.user.service

import com.vehicle.manager.commons.dto.JwtTokenDTO
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import spock.lang.Specification

class LoginServiceSpec extends Specification {
    AuthenticationManager authenticationManager = Mock(AuthenticationManager)
    JwtEncoder jwtEncoder = Mock(JwtEncoder)
    LoginService loginService = new LoginService(authenticationManager, jwtEncoder)

    def "logIn_ValidEmailAndPassword_ReturnsToken"() {
        given:
        def email = "test@example.com"
        def password = "password123"
        def authentication = Mock(Authentication)
        def jwt = Mock(Jwt)

        when:
        def result = loginService.logIn(email, password)

        then:
        1 * authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)) >> authentication
        1 * authentication.getName() >> email
        1 * jwtEncoder.encode(_ as JwtEncoderParameters) >> {
            def claims = it[0].getClaims().getClaims()
            claims["sub"] == email
            claims["exp"] == null
            claims["scope"] == "USER"
            return jwt
        }
        1 * jwt.getTokenValue() >> "tokenValue"
        0 * _
        result == new JwtTokenDTO("tokenValue")
    }

    def "logIn_InvalidPassword_ThrowsAuthenticationException"() {
        given:
        def email = "test@example.com"
        def password = "wrongpassword"

        when:
        loginService.logIn(email, password)

        then:
        1 * authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)) >> {
            throw new BadCredentialsException("Invalid credentials")
        }
        0 * _
        thrown AuthenticationException
    }
}