package com.vehicle.manager.user.controller

import com.vehicle.manager.user.repository.TokenRepository
import com.vehicle.manager.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.MySQLContainer
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TokenControllerSpec extends Specification {
    private static final MySQLContainer mySQLContainer
    @Autowired
    private MockMvc mockMvc
    @Autowired
    private ResourceLoader resourceLoader
    @Autowired
    private UserRepository userRepository
    @Autowired
    private TokenRepository tokenRepository

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

    def "Fail when trying to delete not existing token but it still returns no content"() {
        expect:
        mockMvc.perform(delete("/api/v1/tokens/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql(["/sql/insertUser.sql", "/sql/insertToken.sql"])
    def "Success when trying to delete existing token"() {
        expect:
        mockMvc.perform(delete("/api/v1/tokens/ea7505dd-9733-11ee-a97f-0242ac110003")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath('$').doesNotExist())
    }
}
