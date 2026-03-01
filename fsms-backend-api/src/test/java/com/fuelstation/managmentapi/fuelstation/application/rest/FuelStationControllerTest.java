package com.fuelstation.managmentapi.fuelstation.application.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fuelstation.managmentapi.common.WithMockCustomUser;
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
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

        @Test
        @WithMockCustomUser
        @DisplayName("Should create fuel station")
        public void shouldCreateFuelStation() throws Exception {
            FuelStationResponse fuelStationResponse = fuelStationTestClient.createFuelStationAndReturnResponse();
            assertThat(fuelStationResponse.getBuildingNumber()).isEqualTo("Test");
        }

        @ParameterizedTest
        @MethodSource("invalidCreateFuelStationRequests")
        @WithMockCustomUser
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

        @Test
        @WithMockCustomUser
        @DisplayName("Should deactivate fuel station")
        public void shouldDeactivateFuelStation() throws Exception {
            FuelStationResponse fuelStationResponse = fuelStationTestClient.deactivateFuelStationAndReturnResponse(testFuelStation.getFuelStationId());
            assertThat(fuelStationResponse.getStatus()).isEqualTo(FuelStationStatus.DEACTIVATED.toString());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when the fuel station is already deactivated")
        public void shouldReturnConflictWhenFuelStationIsAlreadyDeactivated() throws Exception {
            fuelStationTestClient.deactivateFuelStation(testFuelStation.getFuelStationId()).andExpect(status().isOk());
            fuelStationTestClient.deactivateFuelStation(testFuelStation.getFuelStationId()).andExpect(status().isConflict());
        }

        @Test
        @WithMockCustomUser
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

        @Test
        @WithMockCustomUser
        @DisplayName("Should assign manager to fuel station")
        public void shouldAssignManagerToFuelStation() throws Exception {
            var assignedManger = fuelStationTestClient.assignManagerToFuelStationAndReturnResponse(testFuelStation.getFuelStationId(), testManager.getManagerId());
            var fuelStation = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getFuelStationId());

            assertThat(fuelStation.getAssignedManagersIds()).contains(assignedManger.getManagerId());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when the manager is already assigned")
        public void shouldReturnConflictWhenManagerIsAlreadyAssigned() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getFuelStationId(), testManager.getManagerId()).andExpect(status().isOk());
            fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getFuelStationId(), testManager.getManagerId()).andExpect(status().isConflict());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(99999L, testManager.getManagerId()).andExpect(status().isNotFound());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the manager does not exist")
        public void shouldReturnNotFoundWhenManagerDoesNotExist() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getFuelStationId(), 99999L).andExpect(status().isNotFound());
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

        @Test
        @WithMockCustomUser
        @DisplayName("Should unassign manager from fuel station")
        public void shouldUnassignManagerFromFuelStation() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getFuelStationId(), testManager.getManagerId());
            fuelStationTestClient.unassignManagerFromFuelStationAndReturnResponse(testFuelStation.getFuelStationId(), testManager.getManagerId());
            var fuelStation = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getFuelStationId());
            assertThat(fuelStation.getAssignedManagersIds()).isEmpty();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when the manager is not assigned ")
        public void shouldReturnConflictWhenManagerIsNotAssigned() throws Exception {
            fuelStationTestClient.unassignManagerFromFuelStation(testFuelStation.getFuelStationId(), testManager.getManagerId()).andExpect(status().isOk());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(99999L, testManager.getManagerId()).andExpect(status().isNotFound());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the manager does not exist")
        public void shouldReturnNotFoundWhenManagerDoesNotExist() throws Exception {
            fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getFuelStationId(), 99999L).andExpect(status().isNotFound());
        }

    }


    @Nested
    class ChangeFuelPriceTests {

        private FuelStationResponse testFuelStation;

        @BeforeEach
        public void setup() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should change fuel price")
        public void shouldChangeFuelPrice() throws Exception {
            ChangeFuelPriceRequest changeFuelPriceRequest = new ChangeFuelPriceRequest(FuelGrade.RON_92, BigDecimal.valueOf(10));
            FuelStationResponse fuelStationResponse = fuelStationTestClient.changeFuelPriceAndReturnResponse(testFuelStation.getFuelStationId(), changeFuelPriceRequest);

            Optional<FuelStationResponse.FuelPriceResponse> updatedFuelPrice = fuelStationResponse.getFuelPrices()
                    .stream()
                    .filter(fuelPriceResponse -> fuelPriceResponse.fuelGrade().equals(FuelGrade.RON_92.toString()))
                    .findFirst();

            if (updatedFuelPrice.isPresent()) {
                assertThat(updatedFuelPrice.get().pricePerLiter()).isEqualTo(BigDecimal.valueOf(10));
            } else {
                assertThat(updatedFuelPrice).isNotNull();
            }
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.changeFuelPrice(99999L, new ChangeFuelPriceRequest(FuelGrade.RON_92, BigDecimal.valueOf(10))).andExpect(status().isNotFound());
        }

    }

    @Nested
    class GetFuelStationByIdTests {

        private FuelStationResponse testFuelStation;

        @BeforeEach
        public void setup() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should get fuel station by id")
        public void shouldGetFuelStationById() throws Exception {
            FuelStationResponse fuelStationResponse = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getFuelStationId());
            assertThat(fuelStationResponse.getFuelStationId()).isEqualTo(testFuelStation.getFuelStationId());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.getFuelStationById(99999L).andExpect(status().isNotFound());
        }

    }

    @Test
    @WithMockCustomUser
    @DisplayName("Should get all fuel stations")
    public void shouldGetAllFuelStations() throws Exception {
        fuelStationTestClient.createFuelStationAndReturnResponse();
        List<?> fuelStationResponses = fuelStationTestClient.getAllFuelStationsAndReturnResponse();
        assertThat(fuelStationResponses.size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Should get all managers assigned to the fuel station")
    public void shouldGetAllManagersAssignedToFuelStation() throws Exception {
        ManagerResponse testManager = managerTestClient.createManagerAndReturnResponse();
        FuelStationResponse testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();

        fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getFuelStationId(), testManager.getManagerId());
        List<ManagerResponse> managers = fuelStationTestClient.getManagersAssignedToFuelStationAndReturnResponse(testFuelStation.getFuelStationId());
        assertThat(managers.size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Should get all fuel orders related to the fuel station")
    public void shouldGetAllFuelOrdersRelatedToFuelStation() throws Exception {
        FuelStationResponse testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();

        fuelOrderTestClient.createFuelOrder(new CreateFuelOrderRequest(testFuelStation.getFuelStationId(), FuelGrade.RON_92, BigDecimal.valueOf(10)));
        fuelOrderTestClient.createFuelOrder(new CreateFuelOrderRequest(testFuelStation.getFuelStationId(), FuelGrade.DIESEL, BigDecimal.valueOf(10)));

        List<FuelOrderResponse> fuelOrders = fuelStationTestClient.getFuelStationFuelOrdersAndReturnResponse(testFuelStation.getFuelStationId());
        assertThat(fuelOrders.size()).isEqualTo(2);
    }

}