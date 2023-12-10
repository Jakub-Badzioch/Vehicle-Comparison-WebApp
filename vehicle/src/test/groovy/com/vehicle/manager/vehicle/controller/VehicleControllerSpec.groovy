package com.vehicle.manager.vehicle.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.vehicle.manager.vehicle.repository.VehicleRepository
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.MySQLContainer
import spock.lang.Specification

import java.nio.charset.Charset

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/clearDatabase.sql")
class VehicleControllerSpec extends Specification {

    private static final MySQLContainer mySQLContainer
    @Autowired
    private MockMvc mockMvc
    @Autowired
    private ResourceLoader resourceLoader
    @Autowired
    private VehicleRepository vehicleRepository
    @Autowired
    private ObjectMapper objectMapper

    static {
        mySQLContainer = new MySQLContainer("mysql:8.0")
                .withUsername("exampleUserName")
                .withPassword("examplePassword")
                .withDatabaseName("vehicle-service")
        mySQLContainer.start()
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dpr) {
        dpr.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl())
        dpr.add("spring.datasource.username", () -> mySQLContainer.getUsername())
        dpr.add("spring.datasource.password", () -> mySQLContainer.getPassword())
    }

    @Sql("/sql/insertVehicle.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/clearDatabase.sql")
    def "Success when someone is trying to filter vehicles giving no filter criteria and getting 1 vehicle back"() {
        expect:
        mockMvc.perform(post("/api/v1/vehicles/search")
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/PageableFilteringAndPagingDTO.json")
                        .getInputStream(), Charset.defaultCharset()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        IOUtils.toString(resourceLoader.getResource("classpath:json/expected_response_1.json")
                                .getInputStream(), Charset.defaultCharset()
                        ))
                )
    }

    @Sql("/sql/insert4Vehicles.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/clearDatabase.sql")
    def "Success when someone is trying to filter vehicles giving as input filter criteria that 2 vehicle (out of 4) meets"() {
        expect:
        mockMvc.perform(post("/api/v1/vehicles/search")
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/BIG_FilteringAndPagingDTO.json")
                        .getInputStream(), Charset.defaultCharset()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        IOUtils.toString(resourceLoader.getResource("classpath:json/expected_response_2.json")
                                .getInputStream(), Charset.defaultCharset()
                        ))
                )
    }

    def "Fail when someone is trying to filter vehicles giving as input invalid filter criteria"() {
        expect:
        mockMvc.perform(post("/api/v1/vehicles/search")
                .content(IOUtils.toString(resourceLoader.getResource("classpath:json/INVALID_FilteringAndPagingDTO.json")
                        .getInputStream(), Charset.defaultCharset()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
    }

    @WithMockUser(authorities = 'SCOPE_USER', username = 'bodziov3@gmail.com')
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/clearDatabase.sql")
    def "Success when logged in user is trying to add vehicle"() {
        expect:
        mockMvc.perform(
                multipart(HttpMethod.POST, "/api/v1/vehicles")
                        .file(new MockMultipartFile("images", "volkswagen_1.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[10]))
                        .file(new MockMultipartFile("images", "volkswagen_2.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[10]))
                        .file(new MockMultipartFile(
                                "vehicleDTO", "", MediaType.APPLICATION_JSON_VALUE,
                                resourceLoader.getResource("classpath:json/VehicleDTO.json").getInputStream()
                        ))
        ).andExpect(status().isCreated())
    }

    def "Fail when someone is trying to add vehicle without logging in"() {
        expect:
        mockMvc.perform(
                multipart(HttpMethod.POST, "/api/v1/vehicles")
                        .file(new MockMultipartFile("images", "volkswagen_1.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[10]))
                        .file(new MockMultipartFile("images", "volkswagen_2.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[10]))
                        .file(new MockMultipartFile("vehicleDTO", "",
                                MediaType.APPLICATION_JSON_VALUE,
                                resourceLoader.getResource("classpath:json/VehicleDTO.json").getInputStream()
                        ))
        ).andExpect(status().isUnauthorized())
    }

    @WithMockUser(authorities = 'SCOPE_USER', username = 'bodziov3@gmail.com')
    def "Fail when logged in user is trying to add vehicle with invalid images"() {
        expect:
        mockMvc.perform(
                multipart(HttpMethod.POST, "/api/v1/vehicles")
                        .file(new MockMultipartFile("images", "volkswagen_1.png", MediaType.IMAGE_PNG_VALUE, new byte[300]))
                        .file(new MockMultipartFile("images", "volkswagen_2.png", MediaType.IMAGE_PNG_VALUE, new byte[300]))
                        .file(new MockMultipartFile("vehicleDTO", "", MediaType.APPLICATION_JSON_VALUE, resourceLoader.getResource("classpath:json/VehicleDTO.json").getInputStream())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.message').value("Every image must be .jpg and smaller or equal to 200 bytes."))
    }

    @Sql("/sql/insertVehicle.sql")
    @WithMockUser(authorities = 'SCOPE_ADMIN', username = "jan.pan@gmail.com")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/clearDatabase.sql")
    def "Success when logged in admin is trying to update vehicle which he did not create"() {
        expect:
        mockMvc.perform(put("/api/v1/vehicles/0bc9d8e2-35f4-a4b3-c88d-72a1b3c4d5a9")
                .content(IOUtils.toString(
                        resourceLoader.getResource("classpath:json/VehicleDTO_2.json").getInputStream(),
                        Charset.defaultCharset()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        IOUtils.toString(resourceLoader.getResource("classpath:json/expected_response_4.json")
                                .getInputStream(), Charset.defaultCharset()
                        ))
                )
    }

    @Sql("/sql/insertVehicle.sql")
    @WithMockUser(authorities = 'SCOPE_USER', username = "jan.pan@gmail.com")
    def "Fail when logged in user is trying to update vehicle which he did not create"() {
        expect:
        mockMvc.perform(put("/api/v1/vehicles/0bc9d8e2-35f4-a4b3-c88d-72a1b3c4d5a9")
                .content(IOUtils.toString(
                        resourceLoader.getResource("classpath:json/VehicleDTO_2.json").getInputStream(),
                        Charset.defaultCharset()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @WithMockUser(authorities = 'SCOPE_ADMIN', username = "jan.pan@gmail.com")
    def "Fail when logged in admin is trying to update not existing vehicle"() {
        expect:
        mockMvc.perform(put("/api/v1/vehicles/" + UUID.randomUUID())
                .content(IOUtils.toString(
                        resourceLoader.getResource("classpath:json/VehicleDTO_2.json").getInputStream(),
                        Charset.defaultCharset()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('$.message').value("Data couldn't be found"))
    }

    @WithMockUser(authorities = 'SCOPE_ADMIN', username = "jan.pan@gmail.com")
    @Sql("/sql/insertVehicle.sql")
    def "Success when logged in admin is trying to delete existing vehicle which he did not create"() {
        expect:
        mockMvc.perform(delete("/api/v1/vehicles/0bc9d8e2-35f4-a4b3-c88d-72a1b3c4d5a9"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @WithMockUser(authorities = 'SCOPE_USER', username = "bodziov3@gmail.com")
    @Sql("/sql/insertVehicle.sql")
    def "Success when logged in user is trying to delete existing vehicle which he created"() {
        expect:
        mockMvc.perform(delete("/api/v1/vehicles/0bc9d8e2-35f4-a4b3-c88d-72a1b3c4d5a9"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @WithMockUser(authorities = 'SCOPE_USER', username = "jan.pan@gmail.com")
    def "Fail when logged in user is trying to delete not existing vehicle"() {
        expect:
        mockMvc.perform(delete("/api/v1/vehicles/" + UUID.randomUUID()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @Sql("/sql/insertVehicle.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/clearDatabase.sql")
    @WithMockUser(authorities = 'SCOPE_USER', username = "jan.pan@gmail.com")
    def "Fail when logged in user is trying to delete existing vehicle which he did not create"() {
        expect:
        mockMvc.perform(delete("/api/v1/vehicles/" + UUID.fromString("0bc9d8e2-35f4-a4b3-c88d-72a1b3c4d5a9")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath('$').doesNotExist())
    }

    @WithMockUser(authorities = 'SCOPE_USER', username = "bodziov3@gmail.com")
    @Sql("/sql/insertVehicle.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/sql/clearDatabase.sql")
    def "Success when logged in user is trying to get all vehicles created by him"() {
        expect:
        mockMvc.perform(get("/api/v1/vehicles/all-yours")
                .param("pageNumber", "0")
                .param("pageSize", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        IOUtils.toString(resourceLoader.getResource("classpath:json/expected_response_3.json")
                                .getInputStream(), Charset.defaultCharset()
                        ))
                )
    }

    def "Fail when someone is trying to get all vehicles created by him without logging in"() {
        expect:
        mockMvc.perform(get("/api/v1/vehicles/all-yours")
                .param("pageNumber", "0")
                .param("pageSize", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath('$').doesNotExist())
    }
}