package com.fuelstation.managmentapi.fuelstation.application.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fuelstation.managmentapi.common.WithMockCustomUser;
import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.application.rest.CreateFuelOrderRequest;
import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderResponse;
import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderTestClient;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.CreateFuelStationRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.ChangeFuelPriceRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.ChangeFuelPricesBulkRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.DispenseFuelRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.InstallFuelTankRequest;
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
                    Arguments.of(new CreateFuelStationRequest(null, "Street", "City", "12345", CountryCode.UA), "null building number"),
                    Arguments.of(new CreateFuelStationRequest("", "Street", "City", "12345", CountryCode.UA), "empty building number"),
                    Arguments.of(new CreateFuelStationRequest("   ", "Street", "City", "12345", CountryCode.UA), "blank building number"),
                    Arguments.of(new CreateFuelStationRequest("123", null, "City", "12345", CountryCode.UA), "null street"),
                    Arguments.of(new CreateFuelStationRequest("123", "", "City", "12345", CountryCode.UA), "empty street"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", null, "12345", CountryCode.UA), "null city"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", "", "12345", CountryCode.UA), "empty city"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", "City", null, CountryCode.UA), "null postal code"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", "City", "", CountryCode.UA), "empty postal code"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", "City", "invalid", CountryCode.UA), "invalid postal code format"),
                    Arguments.of(new CreateFuelStationRequest("123", "Street", "City", "12345", null), "null country"),
                    Arguments.of(new CreateFuelStationRequest("A".repeat(256), "Street", "City", "12345", CountryCode.UA), "building number too long"),
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
    class UpdateFuelPriceTests {

        private FuelStationResponse testFuelStation;

        @BeforeEach
        public void setup() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should update fuel price for a single grade")
        public void shouldUpdateFuelPrice() throws Exception {
            ChangeFuelPriceRequest request = new ChangeFuelPriceRequest(BigDecimal.valueOf(10));
            FuelStationResponse fuelStationResponse = fuelStationTestClient.updateFuelPriceAndReturnResponse(
                    testFuelStation.getFuelStationId(), FuelGrade.RON_92, request);

            Optional<FuelStationResponse.FuelPriceResponse> updatedFuelPrice = fuelStationResponse.getFuelPrices()
                    .stream()
                    .filter(fuelPriceResponse -> fuelPriceResponse.fuelGrade().equals(FuelGrade.RON_92.toString()))
                    .findFirst();

            assertThat(updatedFuelPrice).isPresent();
            assertThat(updatedFuelPrice.get().pricePerLiter()).isEqualTo(BigDecimal.valueOf(10));
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.updateFuelPrice(99999L, FuelGrade.RON_92, new ChangeFuelPriceRequest(BigDecimal.valueOf(10)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Bad Request when the fuel grade in the path is unknown")
        public void shouldReturnBadRequestWhenFuelGradeUnknown() throws Exception {
            fuelStationTestClient.updateFuelPriceRaw(testFuelStation.getFuelStationId(), "unknown-grade",
                    new ChangeFuelPriceRequest(BigDecimal.valueOf(10)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource("invalidUpdateFuelPriceRequests")
        @WithMockCustomUser
        @DisplayName("Should return Bad Request for invalid requests")
        public void shouldReturnBadRequestForInvalidRequests(ChangeFuelPriceRequest request) throws Exception {
            fuelStationTestClient.updateFuelPrice(testFuelStation.getFuelStationId(), FuelGrade.RON_92, request)
                    .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> invalidUpdateFuelPriceRequests() {
            return Stream.of(
                    Arguments.of(new ChangeFuelPriceRequest(null), "null price"),
                    Arguments.of(new ChangeFuelPriceRequest(BigDecimal.ZERO), "zero price"),
                    Arguments.of(new ChangeFuelPriceRequest(BigDecimal.valueOf(-1)), "negative price")
            );
        }

    }

    @Nested
    class UpdateFuelPricesTests {

        private FuelStationResponse testFuelStation;

        @BeforeEach
        public void setup() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should bulk update fuel prices for all grades")
        public void shouldBulkUpdateFuelPrices() throws Exception {
            ChangeFuelPricesBulkRequest request = new ChangeFuelPricesBulkRequest(List.of(
                    new ChangeFuelPricesBulkRequest.FuelPriceUpdate(FuelGrade.RON_92, BigDecimal.valueOf(11)),
                    new ChangeFuelPricesBulkRequest.FuelPriceUpdate(FuelGrade.RON_95, BigDecimal.valueOf(12)),
                    new ChangeFuelPricesBulkRequest.FuelPriceUpdate(FuelGrade.DIESEL, BigDecimal.valueOf(13))
            ));
            FuelStationResponse fuelStationResponse = fuelStationTestClient.updateFuelPricesAndReturnResponse(
                    testFuelStation.getFuelStationId(), request);

            assertThat(priceFor(fuelStationResponse, FuelGrade.RON_92)).isEqualTo(BigDecimal.valueOf(11));
            assertThat(priceFor(fuelStationResponse, FuelGrade.RON_95)).isEqualTo(BigDecimal.valueOf(12));
            assertThat(priceFor(fuelStationResponse, FuelGrade.DIESEL)).isEqualTo(BigDecimal.valueOf(13));
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should bulk update only a subset of fuel grades")
        public void shouldBulkUpdateSubset() throws Exception {
            BigDecimal originalRon95 = priceFor(testFuelStation, FuelGrade.RON_95);
            ChangeFuelPricesBulkRequest request = new ChangeFuelPricesBulkRequest(List.of(
                    new ChangeFuelPricesBulkRequest.FuelPriceUpdate(FuelGrade.RON_92, BigDecimal.valueOf(7))
            ));
            FuelStationResponse fuelStationResponse = fuelStationTestClient.updateFuelPricesAndReturnResponse(
                    testFuelStation.getFuelStationId(), request);

            assertThat(priceFor(fuelStationResponse, FuelGrade.RON_92)).isEqualTo(BigDecimal.valueOf(7));
            assertThat(priceFor(fuelStationResponse, FuelGrade.RON_95)).isEqualTo(originalRon95);
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            ChangeFuelPricesBulkRequest request = new ChangeFuelPricesBulkRequest(List.of(
                    new ChangeFuelPricesBulkRequest.FuelPriceUpdate(FuelGrade.RON_92, BigDecimal.valueOf(10))
            ));
            fuelStationTestClient.updateFuelPrices(99999L, request).andExpect(status().isNotFound());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Bad Request when prices list is empty")
        public void shouldReturnBadRequestWhenPricesEmpty() throws Exception {
            fuelStationTestClient.updateFuelPrices(testFuelStation.getFuelStationId(),
                    new ChangeFuelPricesBulkRequest(List.of()))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Bad Request when an item has a non-positive price")
        public void shouldReturnBadRequestWhenItemPriceInvalid() throws Exception {
            ChangeFuelPricesBulkRequest request = new ChangeFuelPricesBulkRequest(List.of(
                    new ChangeFuelPricesBulkRequest.FuelPriceUpdate(FuelGrade.RON_92, BigDecimal.valueOf(-5))
            ));
            fuelStationTestClient.updateFuelPrices(testFuelStation.getFuelStationId(), request)
                    .andExpect(status().isBadRequest());
        }

        private static BigDecimal priceFor(FuelStationResponse response, FuelGrade grade) {
            return response.getFuelPrices().stream()
                    .filter(fp -> fp.fuelGrade().equals(grade.toString()))
                    .findFirst()
                    .map(FuelStationResponse.FuelPriceResponse::pricePerLiter)
                    .orElseThrow();
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

    @Nested
    class InstallFuelTankTests {

        private FuelStationResponse testFuelStation;

        @BeforeEach
        public void setup() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should install a fuel tank")
        public void shouldInstallFuelTank() throws Exception {
            int initialTanksCount = testFuelStation.getFuelTanks().size();

            FuelStationResponse response = fuelStationTestClient.installFuelTankAndReturnResponse(
                    testFuelStation.getFuelStationId(),
                    new InstallFuelTankRequest(FuelGrade.RON_95, BigDecimal.valueOf(50000))
            );

            assertThat(response.getFuelTanks()).hasSize(initialTanksCount + 1);
            FuelStationResponse.FuelTankResponse installed = response.getFuelTanks().stream()
                    .reduce((a, b) -> a.id() > b.id() ? a : b)
                    .orElseThrow();
            assertThat(installed.fuelGrade()).isEqualTo(FuelGrade.RON_95.toString());
            assertThat(installed.maxCapacity()).isEqualByComparingTo(BigDecimal.valueOf(50000));
            assertThat(installed.currentVolume()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.installFuelTank(99999L,
                    new InstallFuelTankRequest(FuelGrade.RON_95, BigDecimal.valueOf(35000)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when the fuel station is deactivated")
        public void shouldReturnConflictWhenFuelStationIsDeactivated() throws Exception {
            fuelStationTestClient.deactivateFuelStation(testFuelStation.getFuelStationId())
                    .andExpect(status().isOk());

            fuelStationTestClient.installFuelTank(testFuelStation.getFuelStationId(),
                    new InstallFuelTankRequest(FuelGrade.RON_95, BigDecimal.valueOf(35000)))
                    .andExpect(status().isConflict());
        }

        @ParameterizedTest
        @MethodSource("invalidInstallRequests")
        @WithMockCustomUser
        @DisplayName("Should return Bad Request for invalid requests")
        public void shouldReturnBadRequestForInvalidRequests(InstallFuelTankRequest request) throws Exception {
            fuelStationTestClient.installFuelTank(testFuelStation.getFuelStationId(), request)
                    .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> invalidInstallRequests() {
            return Stream.of(
                    Arguments.of(new InstallFuelTankRequest(null, BigDecimal.valueOf(35000)), "null fuel grade"),
                    Arguments.of(new InstallFuelTankRequest(FuelGrade.RON_92, null), "null capacity"),
                    Arguments.of(new InstallFuelTankRequest(FuelGrade.RON_92, BigDecimal.ZERO), "zero capacity"),
                    Arguments.of(new InstallFuelTankRequest(FuelGrade.RON_92, BigDecimal.valueOf(-1)), "negative capacity")
            );
        }

    }

    @Nested
    class DispenseFuelTests {

        private FuelStationResponse testFuelStation;

        @BeforeEach
        public void setup() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should dispense fuel from a tank")
        public void shouldDispenseFuel() throws Exception {
            FuelOrderResponse order = fuelOrderTestClient.createFuelOrderAndReturnResponse(new CreateFuelOrderRequest(
                    testFuelStation.getFuelStationId(),
                    FuelGrade.RON_92,
                    BigDecimal.valueOf(100)
            ));
            fuelOrderTestClient.confirmFuelOrderAndReturnResponse(order.getFuelOrderId());

            FuelStationResponse refilled = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getFuelStationId());
            FuelStationResponse.FuelTankResponse ron92Tank = refilled.getFuelTanks().stream()
                    .filter(t -> t.fuelGrade().equals(FuelGrade.RON_92.toString()))
                    .findFirst()
                    .orElseThrow();

            assertThat(ron92Tank.currentVolume()).isEqualByComparingTo(BigDecimal.valueOf(100));

            FuelStationResponse response = fuelStationTestClient.dispenseFuelAndReturnResponse(
                    testFuelStation.getFuelStationId(),
                    ron92Tank.id(),
                    new DispenseFuelRequest(BigDecimal.valueOf(30))
            );

            FuelStationResponse.FuelTankResponse afterDispense = response.getFuelTanks().stream()
                    .filter(t -> t.id().equals(ron92Tank.id()))
                    .findFirst()
                    .orElseThrow();
            assertThat(afterDispense.currentVolume()).isEqualByComparingTo(BigDecimal.valueOf(70));
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when the tank has insufficient fuel")
        public void shouldReturnConflictWhenInsufficientVolume() throws Exception {
            long tankId = testFuelStation.getFuelTanks().get(0).id();
            fuelStationTestClient.dispenseFuel(
                    testFuelStation.getFuelStationId(),
                    tankId,
                    new DispenseFuelRequest(BigDecimal.valueOf(10))
            ).andExpect(status().isConflict());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when requested volume exceeds available volume after a partial refill")
        public void shouldReturnConflictWhenRequestedVolumeExceedsAvailable() throws Exception {
            FuelOrderResponse order = fuelOrderTestClient.createFuelOrderAndReturnResponse(new CreateFuelOrderRequest(
                    testFuelStation.getFuelStationId(),
                    FuelGrade.RON_92,
                    BigDecimal.valueOf(500)
            ));
            fuelOrderTestClient.confirmFuelOrderAndReturnResponse(order.getFuelOrderId());

            FuelStationResponse refilled = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getFuelStationId());
            FuelStationResponse.FuelTankResponse ron92Tank = refilled.getFuelTanks().stream()
                    .filter(t -> t.fuelGrade().equals(FuelGrade.RON_92.toString()))
                    .findFirst()
                    .orElseThrow();
            assertThat(ron92Tank.currentVolume()).isEqualByComparingTo(BigDecimal.valueOf(500));

            fuelStationTestClient.dispenseFuel(
                    testFuelStation.getFuelStationId(),
                    ron92Tank.id(),
                    new DispenseFuelRequest(BigDecimal.valueOf(1000))
            ).andExpect(status().isConflict());

            FuelStationResponse afterFailedDispense = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getFuelStationId());
            FuelStationResponse.FuelTankResponse tankAfter = afterFailedDispense.getFuelTanks().stream()
                    .filter(t -> t.id().equals(ron92Tank.id()))
                    .findFirst()
                    .orElseThrow();
            assertThat(tankAfter.currentVolume()).isEqualByComparingTo(BigDecimal.valueOf(500));
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel tank does not exist")
        public void shouldReturnNotFoundWhenFuelTankDoesNotExist() throws Exception {
            fuelStationTestClient.dispenseFuel(
                    testFuelStation.getFuelStationId(),
                    99999L,
                    new DispenseFuelRequest(BigDecimal.valueOf(10))
            ).andExpect(status().isNotFound());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.dispenseFuel(99999L, 1L, new DispenseFuelRequest(BigDecimal.valueOf(10)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when the fuel station is deactivated")
        public void shouldReturnConflictWhenFuelStationIsDeactivated() throws Exception {
            long tankId = testFuelStation.getFuelTanks().get(0).id();
            fuelStationTestClient.deactivateFuelStation(testFuelStation.getFuelStationId())
                    .andExpect(status().isOk());

            fuelStationTestClient.dispenseFuel(
                    testFuelStation.getFuelStationId(),
                    tankId,
                    new DispenseFuelRequest(BigDecimal.valueOf(10))
            ).andExpect(status().isConflict());
        }

        @ParameterizedTest
        @MethodSource("invalidDispenseFuelRequests")
        @WithMockCustomUser
        @DisplayName("Should return Bad Request for invalid requests")
        public void shouldReturnBadRequestForInvalidRequests(DispenseFuelRequest request) throws Exception {
            long tankId = testFuelStation.getFuelTanks().get(0).id();
            fuelStationTestClient.dispenseFuel(testFuelStation.getFuelStationId(), tankId, request)
                    .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> invalidDispenseFuelRequests() {
            return Stream.of(
                    Arguments.of(new DispenseFuelRequest(null), "null volume"),
                    Arguments.of(new DispenseFuelRequest(BigDecimal.ZERO), "zero volume"),
                    Arguments.of(new DispenseFuelRequest(BigDecimal.valueOf(-1)), "negative volume")
            );
        }

    }

    @Nested
    class DecommissionFuelTankTests {

        private FuelStationResponse testFuelStation;

        @BeforeEach
        public void setup() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should decommission a fuel tank and exclude it from subsequent reads")
        public void shouldDecommissionFuelTank() throws Exception {
            long tankId = testFuelStation.getFuelTanks().get(0).id();
            int initialTanksCount = testFuelStation.getFuelTanks().size();

            FuelStationResponse response = fuelStationTestClient.decommissionFuelTankAndReturnResponse(
                    testFuelStation.getFuelStationId(), tankId
            );

            assertThat(response.getFuelTanks()).hasSize(initialTanksCount - 1);
            assertThat(response.getFuelTanks().stream().map(FuelStationResponse.FuelTankResponse::id))
                    .doesNotContain(tankId);

            FuelStationResponse reread = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getFuelStationId());
            assertThat(reread.getFuelTanks().stream().map(FuelStationResponse.FuelTankResponse::id))
                    .doesNotContain(tankId);
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the tank has already been decommissioned (it is no longer hydrated into the aggregate)")
        public void shouldReturnNotFoundWhenTankAlreadyDecommissioned() throws Exception {
            long tankId = testFuelStation.getFuelTanks().get(0).id();
            fuelStationTestClient.decommissionFuelTank(testFuelStation.getFuelStationId(), tankId)
                    .andExpect(status().isOk());
            fuelStationTestClient.decommissionFuelTank(testFuelStation.getFuelStationId(), tankId)
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel tank does not exist")
        public void shouldReturnNotFoundWhenFuelTankDoesNotExist() throws Exception {
            fuelStationTestClient.decommissionFuelTank(testFuelStation.getFuelStationId(), 99999L)
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelStationTestClient.decommissionFuelTank(99999L, 1L)
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when the fuel station is deactivated")
        public void shouldReturnConflictWhenFuelStationIsDeactivated() throws Exception {
            long tankId = testFuelStation.getFuelTanks().get(0).id();
            fuelStationTestClient.deactivateFuelStation(testFuelStation.getFuelStationId())
                    .andExpect(status().isOk());

            fuelStationTestClient.decommissionFuelTank(testFuelStation.getFuelStationId(), tankId)
                    .andExpect(status().isConflict());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when the tank is not empty")
        public void shouldReturnConflictWhenTankIsNotEmpty() throws Exception {
            FuelOrderResponse order = fuelOrderTestClient.createFuelOrderAndReturnResponse(new CreateFuelOrderRequest(
                    testFuelStation.getFuelStationId(),
                    FuelGrade.RON_92,
                    BigDecimal.valueOf(100)
            ));
            fuelOrderTestClient.confirmFuelOrderAndReturnResponse(order.getFuelOrderId());

            FuelStationResponse refilled = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getFuelStationId());
            long ron92TankId = refilled.getFuelTanks().stream()
                    .filter(t -> t.fuelGrade().equals(FuelGrade.RON_92.toString()))
                    .findFirst()
                    .orElseThrow()
                    .id();

            fuelStationTestClient.decommissionFuelTank(testFuelStation.getFuelStationId(), ron92TankId)
                    .andExpect(status().isConflict());
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