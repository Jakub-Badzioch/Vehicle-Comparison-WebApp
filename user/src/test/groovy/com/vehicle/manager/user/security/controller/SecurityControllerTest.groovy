package com.vehicle.manager.user.security.controller


import com.vehicle.manager.user.repository.TokenRepository
import com.vehicle.manager.user.repository.UserRepository
import com.vehicle.manager.user.service.RoleService
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.MySQLContainer
import spock.lang.Specification

import java.nio.charset.Charset
import java.time.Duration
import java.time.Instant

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityControllerTest extends Specification {

    private static final MySQLContainer mySQLContainer
    @Autowired
    private MockMvc mockMvc
    @Autowired
    private ResourceLoader resourceLoader
    @Autowired
    private UserRepository userRepository
    @Autowired
    private TokenRepository tokenRepository
    @Autowired
    private RoleService roleService
    @Autowired
    private JwtEncoder jwtEncoder

    static {
        mySQLContainer = new MySQLContainer("mysql:8.0")
                .withUsername("exampleUserName")
                .withPassword("examplePassword")
                .withDatabaseName("vehicle_comparison_site_db")
        mySQLContainer.start()
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dpr) {
        dpr.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl())
        dpr.add("spring.datasource.username", () -> mySQLContainer.getUsername())
        dpr.add("spring.datasource.password", () -> mySQLContainer.getPassword())
    }

    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql("/sql/insertUser.sql")
    def "Success when trying to log in on active account with valid email and password"() {
        expect:
        mockMvc.perform(post("/api/v1/security/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/loginRequestWithCorrectEmailAndPassword.json").getInputStream(), Charset.defaultCharset())))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.token').exists())
    }

    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql("/sql/insertUser.sql")
    def "Fail when trying to log in on active account with invalid password"() {
        expect:
        mockMvc.perform(post("/api/v1/security/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/loginRequestWithIncorrectPassword.json").getInputStream(), Charset.defaultCharset())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath('$.message').value("Bad credentials"))
    }

    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql("/sql/insertUser.sql")
    def "Fail when trying to log in on inactive account with valid email and password"() {
        expect:
        mockMvc.perform(post("/api/v1/security/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/loginRequestWithIncorrectPassword.json").getInputStream(), Charset.defaultCharset())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath('$.message').value("Bad credentials"))
    }







    def "Success logout"() {
        given:
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject("authentication")
                .expiresAt(Instant.now() + Duration.ofDays(1))
                .claim("scope", "USER")
                .build()
        final String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue()

        expect:
        mockMvc.perform(post("/api/v1/security/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$').doesNotExist())
    }
}
