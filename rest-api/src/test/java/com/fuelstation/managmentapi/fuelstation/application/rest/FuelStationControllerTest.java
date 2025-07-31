package com.fuelstation.managmentapi.fuelstation.application.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fuelstation.managmentapi.common.AdminUserTest;
import com.fuelstation.managmentapi.common.WithMockAdminUser;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.application.rest.CreateFuelOrderRequest;
import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderResponse;
import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderTestClient;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.ChangeFuelPriceRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.CreateFuelStationRequest;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;
import com.fuelstation.managmentapi.manager.application.rest.ManagerResponse;
import com.fuelstation.managmentapi.manager.application.rest.ManagerTestClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class FuelStationControllerTest {

    @Autowired
    private FuelStationTestClient fuelStationTestClient;

    @Autowired
    private FuelOrderTestClient fuelOrderTestClient;

    @Autowired
    private ManagerTestClient managerTestClient;

    @Nested
    class CreateFuelStation {

        @AdminUserTest
        @DisplayName("Should create fuel station")
        public void shouldCreateFuelStation() throws Exception {
            FuelStationResponse fuelStationResponse = fuelStationTestClient.createFuelStationAndReturnResponse();
            assertThat(fuelStationResponse.getBuildingNumber()).isEqualTo("Test");
        }

        @ParameterizedTest
        @MethodSource("invalidCreateFuelStationRequests")
        @WithMockAdminUser
        @DisplayName("Should return Bad Request for invalid requests")
        public void shouldReturnBadRequestForInvalidRequests(CreateFuelStationRequest request) throws Exception {
            fuelStationTestClient.createFuelStation(request)
                    .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> invalidCreateFuelStationRequests() {
            return Stream.of(
                    Arguments.of(new CreateFuelStationRequest(null, "Street", "City", "12345", "Ukraine"), "null building number"),
                    Arguments.of(new CreateFuelStationRequest("", "Street", "City", "12345", "Ukraine"), "empty building number"),
                    Arguments.of(new CreateFuelStationRequest("   ", "Street", "City", "12345", "Ukraine"), "blank building number"),
                    Arguments.of(new CreateFuelStationRequest("123", null, "City", "12345", "Ukraine"), "null street"),
                    Arguments.of(new CreateFuelStationRequest("123", "", "City", "12345", "Ukraine"), "empty street"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", null, "12345", "Ukraine"), "null city"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", "", "12345", "Ukraine"), "empty city"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", "City", null, "Ukraine"), "null postal code"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", "City", "", "Ukraine"), "empty postal code"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", "City", "invalid", "Ukraine"), "invalid postal code format"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", "City", "12345", ""), "empty country"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", "City", "12345", null), "null country"),
                    Arguments.of(new CreateFuelStationRequest("A".repeat(256), "Street", "City", "12345", "Ukraine"), "building number too long"),
                    Arguments.of(new CreateFuelStationRequest(null, null, null, null, null), "all fields null")
            );
        }

    }

    @Nested
    class DeactivateFuelStationTests {

        private FuelStationResponse testFuelStation;

        @BeforeEach
        public void setup() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @AdminUserTest
        @DisplayName("Should deactivate fuel station")
        public void shouldDeactivateFuelStation() throws Exception {
            FuelStationResponse fuelStationResponse = fuelStationTestClient.deactivateFuelStationAndReturnResponse(testFuelStation.getId());
            assertThat(fuelStationResponse.getStatus()).isEqualTo(FuelStationStatus.DEACTIVATED.toString());
        }

        @AdminUserTest
        @DisplayName("Should return Conflict when the fuel station is already deactivated")
        public void shouldReturnConflictWhenFuelStationIsAlreadyDeactivated() throws Exception {
            fuelStationTestClient.deactivateFuelStation(testFuelStation.getId()).andExpect(status().isOk());
            fuelStationTestClient.deactivateFuelStation(testFuelStation.getId()).andExpect(status().isConflict());
        }

        @AdminUserTest
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.deactivateFuelStation(99999L).andExpect(status().isNotFound());
        }

    }

    @Nested
    class AssignManagerToFuelStationTests {

        private FuelStationResponse testFuelStation;
        private ManagerResponse testManager;

        @BeforeEach
        public void setup() throws Exception {
            testManager = managerTestClient.createManagerAndReturnResponse();
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @AdminUserTest
        @DisplayName("Should assign manager to fuel station")
        public void shouldAssignManagerToFuelStation() throws Exception {
            FuelStationResponse fuelStationResponse = fuelStationTestClient.assignManagerToFuelStationAndReturnResponse(testFuelStation.getId(), testManager.getId());
            assertThat(fuelStationResponse.getAssignedManagersIds()).contains(testManager.getId());
        }

        @AdminUserTest
        @DisplayName("Should return Conflict when the manager is already assigned")
        public void shouldReturnConflictWhenManagerIsAlreadyAssigned() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getId(), testManager.getId()).andExpect(status().isOk());
            fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getId(), testManager.getId()).andExpect(status().isConflict());
        }

        @AdminUserTest
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(99999L, testManager.getId()).andExpect(status().isNotFound());
        }

        @AdminUserTest
        @DisplayName("Should return Not Found when the manager does not exist")
        public void shouldReturnNotFoundWhenManagerDoesNotExist() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getId(), 99999L).andExpect(status().isNotFound());
        }

    }

    @Nested
    class UnassignManagerFromFuelStationTests {

        private FuelStationResponse testFuelStation;
        private ManagerResponse testManager;

        @BeforeEach
        public void setup() throws Exception {
            testManager = managerTestClient.createManagerAndReturnResponse();
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @AdminUserTest
        @DisplayName("Should unassign manager from fuel station")
        public void shouldUnassignManagerFromFuelStation() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getId(), testManager.getId());
            FuelStationResponse fuelStationResponse = fuelStationTestClient.unassignManagerFromFuelStationAndReturnResponse(testFuelStation.getId(), testManager.getId());
            assertThat(fuelStationResponse.getAssignedManagersIds()).isEmpty();
        }

        @AdminUserTest
        @DisplayName("Should return Conflict when the manager is not assigned ")
        public void shouldReturnConflictWhenManagerIsNotAssigned() throws Exception {
            fuelStationTestClient.unassignManagerFromFuelStation(testFuelStation.getId(), testManager.getId()).andExpect(status().isOk());
        }

        @AdminUserTest
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(99999L, testManager.getId()).andExpect(status().isNotFound());
        }

        @AdminUserTest
        @DisplayName("Should return Not Found when the manager does not exist")
        public void shouldReturnNotFoundWhenManagerDoesNotExist() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getId(), 99999L).andExpect(status().isNotFound());
        }

    }


    @Nested
    class ChangeFuelPriceTests {

        private FuelStationResponse testFuelStation;

        @BeforeEach
        public void setup() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @AdminUserTest
        @DisplayName("Should change fuel price")
        public void shouldChangeFuelPrice() throws Exception {
            ChangeFuelPriceRequest changeFuelPriceRequest = new ChangeFuelPriceRequest(FuelGrade.RON_92, 10f);
            FuelStationResponse fuelStationResponse = fuelStationTestClient.changeFuelPriceAndReturnResponse(testFuelStation.getId(), changeFuelPriceRequest);

            Optional<FuelStationResponse.FuelPriceResponse> updatedFuelPrice = fuelStationResponse.getFuelPrices()
                    .stream()
                    .filter(fuelPriceResponse -> fuelPriceResponse.fuelGrade().equals(FuelGrade.RON_92.toString()))
                    .findFirst();

            if (updatedFuelPrice.isPresent()) {
                assertThat(updatedFuelPrice.get().pricePerLiter()).isEqualTo(10f);
            } else {
                assertThat(updatedFuelPrice).isNotNull();
            }
        }

        @AdminUserTest
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.changeFuelPrice(99999L, new ChangeFuelPriceRequest(FuelGrade.RON_92, 10f)).andExpect(status().isNotFound());
        }

    }

    @Nested
    class GetFuelStationByIdTests {

        private FuelStationResponse testFuelStation;

        @BeforeEach
        public void setup() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @AdminUserTest
        @DisplayName("Should get fuel station by id")
        public void shouldGetFuelStationById() throws Exception {
            FuelStationResponse fuelStationResponse = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getId());
            assertThat(fuelStationResponse.getId()).isEqualTo(testFuelStation.getId());
        }

        @AdminUserTest
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.getFuelStationById(99999L).andExpect(status().isNotFound());
        }

    }

    @AdminUserTest
    @DisplayName("Should get all fuel stations")
    public void shouldGetAllFuelStations() throws Exception {
        fuelStationTestClient.createFuelStationAndReturnResponse();
        List<?> fuelStationResponses = fuelStationTestClient.getAllFuelStationsAndReturnResponse();
        assertThat(fuelStationResponses.size()).isEqualTo(1);
    }

    @AdminUserTest
    @DisplayName("Should get all managers assigned to the fuel station")
    public void shouldGetAllManagersAssignedToFuelStation() throws Exception {
        ManagerResponse testManager = managerTestClient.createManagerAndReturnResponse();
        FuelStationResponse testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();

        fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getId(), testManager.getId());
        List<ManagerResponse> managers = fuelStationTestClient.getManagersAssignedToFuelStationAndReturnResponse(testFuelStation.getId());
        assertThat(managers.size()).isEqualTo(1);
    }

    @AdminUserTest
    @DisplayName("Should get all fuel orders related to the fuel station")
    public void shouldGetAllFuelOrdersRelatedToFuelStation() throws Exception {
        FuelStationResponse testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();

        fuelOrderTestClient.createFuelOrder(new CreateFuelOrderRequest(testFuelStation.getId(), FuelGrade.RON_92, 10f));
        fuelOrderTestClient.createFuelOrder(new CreateFuelOrderRequest(testFuelStation.getId(), FuelGrade.DIESEL, 10f));

        List<FuelOrderResponse> fuelOrders = fuelStationTestClient.getFuelStationFuelOrdersAndReturnResponse(testFuelStation.getId());
        assertThat(fuelOrders.size()).isEqualTo(2);
    }

}