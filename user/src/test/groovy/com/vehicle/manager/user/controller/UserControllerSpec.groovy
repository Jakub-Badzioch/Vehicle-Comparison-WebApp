package com.vehicle.manager.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.vehicle.manager.user.repository.RoleRepository
import com.vehicle.manager.user.repository.TokenRepository
import com.vehicle.manager.user.repository.UserRepository
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.MySQLContainer
import spock.lang.Specification

import java.nio.charset.Charset

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerSpec extends Specification {

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
    private RoleRepository roleRepository
    @Autowired
    private ObjectMapper objectMapper

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
    def "Success when someone is trying to register valid user"() {
        expect:
        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/registerRequestSuccess.json").getInputStream(), Charset.defaultCharset())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath('$.id').exists())
                .andExpect(jsonPath('$.nickName').value("Anda"))
                .andExpect(jsonPath('$.password').doesNotExist())
                .andExpect(jsonPath('$.email').value("andrzej.piaseczny@gmail.com"))
    }

    def "Fail when someone is trying to register user with invalid password and email"() {
        expect:
        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/registerInvalidUser.json").getInputStream(), Charset.defaultCharset())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$[*].fieldName', containsInAnyOrder("password", "email", "nickName"))) // alt enter na metodzie zeby zrobic import
                .andExpect(jsonPath('$[*].message', containsInAnyOrder("Incorrect password. Must be at least: 1 int, 1 lowercase, 1 uppercase, 10 chars.",
                        "Incorrect nickName. Can't be null and length with/without whitespaces must be longer than 0.",
                        "must be a well-formed email address")))
    }

    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql("/sql/insertUser.sql")
    def "Fail when someone is trying to register existing user"() {
        expect:
        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/registerRequestSuccess.json")
                        .getInputStream(), Charset.defaultCharset())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath('$.message').value("Entity already exists"))
    }

    @WithMockUser(authorities = 'SCOPE_ADMIN')
    def "Fail when admin is trying to ask for an admin role"() {
        expect:
        mockMvc.perform(get("/api/v1/users/ask-for-admin-role"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath('$').doesNotExist())
    }

    def "Fail when someone is trying to ask for an admin role without logging in"() {
        expect:
        mockMvc.perform(get("/api/v1/users/ask-for-admin-role"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @WithMockUser(authorities = 'SCOPE_USER', username = 'andrzej.piaseczny@gmail.com')
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql("/sql/insertUser.sql")
    def "Success when logged in user is trying to ask for an admin role"() {
        expect:
        mockMvc.perform(get("/api/v1/users/ask-for-admin-role"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @WithMockUser(authorities = 'SCOPE_USER')
    def "Fail when logged in user is trying to give admin role"() {
        expect:
        mockMvc.perform(put("/api/v1/users/" + UUID.randomUUID() + "/give-admin-role"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @WithMockUser(authorities = 'SCOPE_ADMIN')
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql("/sql/insertUser.sql")
    def "Success when logged in admin is trying to give admin role to existing user which does not have admin role"() {
        expect:
        mockMvc.perform(put("/api/v1/users/0bc9d8e2-35f4-a4b3-c88d-72a1b3c4d5e8/give-admin-role"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @WithMockUser(authorities = 'SCOPE_USER')
    def "Fail when trying to take away admin role from user when user sending request is not an admin"() {
        expect:
        mockMvc.perform(delete("/api/v1/users/" + UUID.randomUUID() + "/roles/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @WithMockUser(authorities = 'SCOPE_ADMIN')
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql("/sql/insertUser.sql")
    def "Fail when trying to take away admin role from user which does not have admin role"() {
        expect:
        mockMvc.perform(delete("/api/v1/users/0bc9d8e2-35f4-a4b3-c88d-72a1b3c4d5e8/roles/0bc9d8e2-35f4-a4b3-c88d-72a1b3c4d5e6")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('$.message').value("Data couldn't be found"))
    }

    @WithMockUser(authorities = 'SCOPE_ADMIN', username = "wiesław.wszywka@gmail.com")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql("/sql/insert2Admins.sql")
    def "Success when trying to take away admin role from user which have admin role"() {
        expect:
        mockMvc.perform(delete("/api/v1/users/0bc9d8e2-35f4-a4b3-c88d-72a1b3c4d5e4/roles/0bc9d8e2-35f4-a4b3-c88d-72a1b3c4d5e6"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @WithMockUser(authorities = 'SCOPE_ADMIN', username = "wiesław.wszywka@gmail.com")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql("/sql/insert2Admins.sql")
    def "Fail when trying to take away admin role from user and giving id of not existing role as path variable"() {
        expect:
        mockMvc.perform(delete("/api/v1/users/0bc9d8e2-35f4-a4b3-c88d-72a1b3c4d5e4/roles/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('$.message').value("Data couldn't be found"))
    }

    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql("/sql/insertUser.sql")
    def "Success when trying to send reset password email for existing user"() {
        expect:
        mockMvc.perform(post("/api/v1/users/send-reset-password-email")
                .param("email", "andrzej.piaseczny@gmail.com"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath('$').doesNotExist())
    }

    def "Fail when trying to send reset password email for not existing user"() {
        expect:
        mockMvc.perform(post("/api/v1/users/send-reset-password-email")
                .param("email", "bodziov3@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('$.message').value("Data couldn't be found"))
    }

    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql(["/sql/insertUser.sql", "/sql/insertPasswordResetToken.sql"])
    def "Success when trying to reset password for existing token of proper type and new password is valid"() {
        expect:
        mockMvc.perform(post("/api/v1/users/reset-password")
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/passwordResetDTO.json")
                        .getInputStream(), Charset.defaultCharset()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/deleteUser.sql")
    @Sql(["/sql/insertUser.sql", "/sql/insertPasswordResetToken.sql"])
    def "Fail when trying to reset password for existing token of proper type and new password is invalid"() {
        expect:
        mockMvc.perform(post("/api/v1/users/reset-password")
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/passwordResetDTOwithInvalidPassword.json")
                        .getInputStream(), Charset.defaultCharset()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$[*].fieldName').value("newPassword"))
                .andExpect(jsonPath('$[*].message').value("Incorrect password. Must be at least: 1 int, 1 lowercase, 1 uppercase, 10 chars."))
    }

    def "Fail when trying to reset password for not existing token"() {
        expect:
        mockMvc.perform(post("/api/v1/users/reset-password")
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/passwordResetDTO.json")
                        .getInputStream(), Charset.defaultCharset()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('$.message').value("Data couldn't be found"))
    }
}