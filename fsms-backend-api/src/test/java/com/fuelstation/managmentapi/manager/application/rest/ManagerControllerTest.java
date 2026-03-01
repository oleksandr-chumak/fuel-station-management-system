package com.fuelstation.managmentapi.manager.application.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fuelstation.managmentapi.common.AdminUserTest;
import com.fuelstation.managmentapi.common.WithMockAdminUser;
import com.fuelstation.managmentapi.authentication.domain.UserStatus;
import com.fuelstation.managmentapi.common.WithMockCustomUser;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ManagerControllerTest {

    @Autowired
    private ManagerTestClient managerTestClient;

    @Nested
    class CreateManager {

        @AdminUserTest
        @DisplayName("Should create manager")
        void shouldCreateManager() throws Exception {
            var manager = managerTestClient.createManagerAndReturnResponse();

            assertThat(manager.getStatus()).isEqualTo(UserStatus.ACTIVE.toString());
        }

        @ParameterizedTest
        @MethodSource("invalidCreateManagerRequests")
        @WithMockAdminUser
        @DisplayName("Should return Bad Request for invalid requests")
        public void shouldReturnBadRequestForInvalidRequests(CreateManagerRequest request) throws Exception {
            managerTestClient.createManager(request)
                    .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> invalidCreateManagerRequests() {
            return Stream.of(
                    // First name validation
                    Arguments.of(new CreateManagerRequest(null, "Doe", "john@example.com"), "null first name"),
                    Arguments.of(new CreateManagerRequest("", "Doe", "john@example.com"), "empty first name"),
                    Arguments.of(new CreateManagerRequest("   ", "Doe", "john@example.com"), "blank first name"),
                    Arguments.of(new CreateManagerRequest("A", "Doe", "john@example.com"), "first name too short"),
                    Arguments.of(new CreateManagerRequest("A".repeat(51), "Doe", "john@example.com"), "first name too long"),
                    Arguments.of(new CreateManagerRequest("John123", "Doe", "john@example.com"), "first name with numbers"),
                    Arguments.of(new CreateManagerRequest("John@", "Doe", "john@example.com"), "first name with special characters"),

                    // Last name validation
                    Arguments.of(new CreateManagerRequest("John", null, "john@example.com"), "null last name"),
                    Arguments.of(new CreateManagerRequest("John", "", "john@example.com"), "empty last name"),
                    Arguments.of(new CreateManagerRequest("John", "   ", "john@example.com"), "blank last name"),
                    Arguments.of(new CreateManagerRequest("John", "D", "john@example.com"), "last name too short"),
                    Arguments.of(new CreateManagerRequest("John", "D".repeat(51), "john@example.com"), "last name too long"),
                    Arguments.of(new CreateManagerRequest("John", "Doe123", "john@example.com"), "last name with numbers"),
                    Arguments.of(new CreateManagerRequest("John", "Doe@", "john@example.com"), "last name with special characters"),

                    // Email validation
                    Arguments.of(new CreateManagerRequest("John", "Doe", null), "null email"),
                    Arguments.of(new CreateManagerRequest("John", "Doe", ""), "empty email"),
                    Arguments.of(new CreateManagerRequest("John", "Doe", "   "), "blank email"),
                    Arguments.of(new CreateManagerRequest("John", "Doe", "invalid-email"), "invalid email format"),
                    Arguments.of(new CreateManagerRequest("John", "Doe", "john@"), "incomplete email"),
                    Arguments.of(new CreateManagerRequest("John", "Doe", "@example.com"), "email without local part"),
                    Arguments.of(new CreateManagerRequest("John", "Doe", "john.example.com"), "email without @ symbol"),
                    Arguments.of(new CreateManagerRequest("John", "Doe", "a".repeat(95) + "@example.com"), "email too long"),

                    // Multiple field validation
                    Arguments.of(new CreateManagerRequest(null, null, null), "all fields null"),
                    Arguments.of(new CreateManagerRequest("", "", ""), "all fields empty"),
                    Arguments.of(new CreateManagerRequest("A", "B", "invalid"), "multiple validation failures")
            );
        }
    }

    @Nested
    class TerminateManagerTests {

        private ManagerResponse testManager;

        @BeforeEach
        void beforeEach() throws Exception {
            testManager = managerTestClient.createManagerAndReturnResponse();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should terminate manager")
        void shouldTerminateManager() throws Exception {
            ManagerResponse managerResponse = managerTestClient.terminateManagerAndReturnResponse(testManager.getManagerId());
            assertThat(managerResponse.getManagerId()).isEqualTo(testManager.getManagerId());
            assertThat(managerResponse.getStatus()).isEqualTo(UserStatus.TERMINATED.toString());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when manger is already terminated")
        void shouldReturnConflictWhenManagerIsAlreadyTerminated() throws Exception {
            managerTestClient.terminateManager(testManager.getManagerId());
            managerTestClient.terminateManager(testManager.getManagerId()).andExpect(status().isConflict());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when manager does not exist")
        void shouldReturnNotFoundWhenManagerDoesNotExist() throws Exception {
            managerTestClient.terminateManager(99999L).andExpect(status().isNotFound());
        }

    }


    @AdminUserTest
    @DisplayName("Should return all managers")
    void shouldReturnAllManagers() throws Exception {
        managerTestClient.getAllManagersAndReturnResponse();
    }

    @Nested
    class GetManagerByUserIdTests {

        private ManagerResponse testManager;

        @BeforeEach
        void beforeEach() throws Exception {
            testManager = managerTestClient.createManagerAndReturnResponse();
        }

        @AdminUserTest
        @DisplayName("Should get manager by id")
        void shouldGetManagerById() throws Exception {
            managerTestClient.getManagerByIdAndReturnResponse(testManager.getManagerId());
        }

        @AdminUserTest
        @DisplayName("Should return Not Found when manager does not exist")
        void shouldReturnNotFoundWhenManagerDoesNotExist() throws Exception {
            managerTestClient.getManagerById(99999L).andExpect(status().isNotFound());
        }

    }

    @Nested
    class GetManagerFuelStationsTests {

        private ManagerResponse testManager;

        @BeforeEach
        void beforeEach() throws Exception {
            testManager = managerTestClient.createManagerAndReturnResponse();
        }

        @AdminUserTest
        @DisplayName("Should get manager fuel stations")
        void shouldGetManagerFuelStations() throws Exception {
            managerTestClient.getManagerFuelStationsAndReturnResponse(testManager.getManagerId());
        }

        @AdminUserTest
        @DisplayName("Should return Not Found when manager does not exist")
        void shouldReturnNotFoundWhenManagerDoesNotExist() throws Exception {
            managerTestClient.getManagerFuelStations(99999L).andExpect(status().isNotFound());
        }

    }

}
