package com.fuelstation.managmentapi.fuelorder.application.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fuelstation.managmentapi.common.WithMockCustomUser;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.application.rest.CreateFuelOrderRequest.AllocationRequest;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;
import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationResponse;
import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationTestClient;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.InstallFuelTankRequest;
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

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class FuelOrderControllerTest {

    @Autowired
    private FuelOrderTestClient fuelOrderTestClient;

    @Autowired
    private FuelStationTestClient fuelStationTestClient;

    private static long tankIdByGrade(FuelStationResponse station, FuelGrade grade) {
        return station.getFuelTanks().stream()
                .filter(t -> t.fuelGrade().equals(grade.toString()))
                .findFirst()
                .orElseThrow()
                .id();
    }

    private static CreateFuelOrderRequest singleAllocation(
            long fuelStationId, FuelGrade grade, long tankId, BigDecimal volume) {
        return new CreateFuelOrderRequest(
                fuelStationId,
                grade,
                List.of(new AllocationRequest(tankId, volume))
        );
    }

    @Nested
    class CreateFuelOrderTests {

        private FuelStationResponse testFuelStation;
        private long ron92TankId;
        private long ron95TankId;
        private long dieselTankId;

        @BeforeEach
        public void setup() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
            ron92TankId = tankIdByGrade(testFuelStation, FuelGrade.RON_92);
            ron95TankId = tankIdByGrade(testFuelStation, FuelGrade.RON_95);
            dieselTankId = tankIdByGrade(testFuelStation, FuelGrade.DIESEL);
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should create a fuel order with a single allocation")
        public void shouldCreateFuelOrderWithSingleAllocation() throws Exception {
            FuelOrderResponse response = fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, ron92TankId, BigDecimal.valueOf(10))
            );

            assertThat(response.getFuelStationId()).isEqualTo(testFuelStation.getFuelStationId());
            assertThat(response.getFuelGrade()).isEqualTo(FuelGrade.RON_92.toString());
            assertThat(response.getStatus()).isEqualTo(FuelOrderStatus.PENDING.toString());
            assertThat(response.getFuelOrderId()).isNotNull();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should create a fuel order with multiple allocations across tanks of the same grade")
        public void shouldCreateFuelOrderWithMultipleAllocations() throws Exception {
            FuelStationResponse afterInstall = fuelStationTestClient.installFuelTankAndReturnResponse(
                    testFuelStation.getFuelStationId(),
                    new InstallFuelTankRequest(FuelGrade.RON_92, BigDecimal.valueOf(35000))
            );
            long secondRon92TankId = afterInstall.getFuelTanks().stream()
                    .filter(t -> t.fuelGrade().equals(FuelGrade.RON_92.toString()))
                    .max(Comparator.comparingLong(FuelStationResponse.FuelTankResponse::id))
                    .orElseThrow()
                    .id();

            FuelOrderResponse response = fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    new CreateFuelOrderRequest(
                            testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92,
                            List.of(
                                    new AllocationRequest(ron92TankId, BigDecimal.valueOf(100)),
                                    new AllocationRequest(secondRon92TankId, BigDecimal.valueOf(200))
                            )
                    )
            );

            assertThat(response.getStatus()).isEqualTo(FuelOrderStatus.PENDING.toString());
            assertThat(response.getFuelGrade()).isEqualTo(FuelGrade.RON_92.toString());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Bad Request when the same tank is allocated twice in one order")
        public void shouldReturnBadRequestForDuplicateAllocations() throws Exception {
            CreateFuelOrderRequest request = new CreateFuelOrderRequest(
                    testFuelStation.getFuelStationId(),
                    FuelGrade.RON_92,
                    List.of(
                            new AllocationRequest(ron92TankId, BigDecimal.valueOf(100)),
                            new AllocationRequest(ron92TankId, BigDecimal.valueOf(50))
                    )
            );

            fuelOrderTestClient.createFuelOrder(request).andExpect(status().isBadRequest());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Bad Request when an allocation references a tank not on the station")
        public void shouldReturnBadRequestWhenTankNotOnStation() throws Exception {
            FuelStationResponse otherStation = fuelStationTestClient.createFuelStationAndReturnResponse();
            long foreignTankId = tankIdByGrade(otherStation, FuelGrade.RON_92);

            fuelOrderTestClient.createFuelOrder(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, foreignTankId, BigDecimal.valueOf(10))
            ).andExpect(status().isBadRequest());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Bad Request when an unknown tank id is referenced")
        public void shouldReturnBadRequestWhenTankUnknown() throws Exception {
            fuelOrderTestClient.createFuelOrder(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, 99999L, BigDecimal.valueOf(10))
            ).andExpect(status().isBadRequest());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Bad Request when the allocated tank has a different fuel grade than the order")
        public void shouldReturnBadRequestWhenTankGradeMismatch() throws Exception {
            fuelOrderTestClient.createFuelOrder(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, dieselTankId, BigDecimal.valueOf(10))
            ).andExpect(status().isBadRequest());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when an allocation volume exceeds the tank's free space")
        public void shouldReturnConflictWhenVolumeExceedsCapacity() throws Exception {
            fuelOrderTestClient.createFuelOrder(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, ron92TankId, BigDecimal.valueOf(50_000))
            ).andExpect(status().isConflict());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should count pending orders against the tank's remaining capacity")
        public void shouldRejectWhenPendingOrdersFillCapacity() throws Exception {
            fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, ron92TankId, BigDecimal.valueOf(30_000))
            );

            fuelOrderTestClient.createFuelOrder(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, ron92TankId, BigDecimal.valueOf(10_000))
            ).andExpect(status().isConflict());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should allow a second order that fits within the remaining capacity after pending reservations")
        public void shouldAllowSecondOrderWithinRemainingCapacity() throws Exception {
            fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, ron92TankId, BigDecimal.valueOf(20_000))
            );

            FuelOrderResponse second = fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, ron92TankId, BigDecimal.valueOf(10_000))
            );

            assertThat(second.getStatus()).isEqualTo(FuelOrderStatus.PENDING.toString());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should not double-count pending volumes when an existing pending order spans multiple matching tanks")
        public void shouldNotDoubleCountPendingVolumes() throws Exception {
            FuelStationResponse afterInstall = fuelStationTestClient.installFuelTankAndReturnResponse(
                    testFuelStation.getFuelStationId(),
                    new InstallFuelTankRequest(FuelGrade.RON_92, BigDecimal.valueOf(35000))
            );
            long secondRon92TankId = afterInstall.getFuelTanks().stream()
                    .filter(t -> t.fuelGrade().equals(FuelGrade.RON_92.toString()))
                    .max(Comparator.comparingLong(FuelStationResponse.FuelTankResponse::id))
                    .orElseThrow()
                    .id();

            fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    new CreateFuelOrderRequest(
                            testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92,
                            List.of(
                                    new AllocationRequest(ron92TankId, BigDecimal.valueOf(10_000)),
                                    new AllocationRequest(secondRon92TankId, BigDecimal.valueOf(10_000))
                            )
                    )
            );

            // With the JOIN+DISTINCT bug, pendingVolumes would double to {tank1: 20_000, tank2: 20_000}
            // and the second 20_000 + 20_000 order would be rejected.
            // After the fix pending is {tank1: 10_000, tank2: 10_000}; 20_000 + 10_000 = 30_000 ≤ 35_000.
            FuelOrderResponse second = fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    new CreateFuelOrderRequest(
                            testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92,
                            List.of(
                                    new AllocationRequest(ron92TankId, BigDecimal.valueOf(20_000)),
                                    new AllocationRequest(secondRon92TankId, BigDecimal.valueOf(20_000))
                            )
                    )
            );

            assertThat(second.getStatus()).isEqualTo(FuelOrderStatus.PENDING.toString());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should ignore pending orders of a different fuel grade in the capacity check")
        public void shouldIgnorePendingOrdersOfDifferentGrade() throws Exception {
            fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_95, ron95TankId, BigDecimal.valueOf(30_000))
            );

            FuelOrderResponse ron92Order = fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, ron92TankId, BigDecimal.valueOf(30_000))
            );

            assertThat(ron92Order.getStatus()).isEqualTo(FuelOrderStatus.PENDING.toString());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should release pending capacity when a prior order is rejected")
        public void shouldReleaseCapacityWhenPriorOrderRejected() throws Exception {
            FuelOrderResponse first = fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, ron92TankId, BigDecimal.valueOf(30_000))
            );
            fuelOrderTestClient.rejectFuelOrderAndReturnResponse(first.getFuelOrderId());

            FuelOrderResponse second = fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, ron92TankId, BigDecimal.valueOf(30_000))
            );

            assertThat(second.getStatus()).isEqualTo(FuelOrderStatus.PENDING.toString());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Conflict when the fuel station is deactivated")
        public void shouldReturnConflictWhenStationDeactivated() throws Exception {
            fuelStationTestClient.deactivateFuelStation(testFuelStation.getFuelStationId());

            fuelOrderTestClient.createFuelOrder(
                    singleAllocation(testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92, ron92TankId, BigDecimal.valueOf(10))
            ).andExpect(status().isConflict());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenStationDoesNotExist() throws Exception {
            fuelOrderTestClient.createFuelOrder(
                    singleAllocation(99999L, FuelGrade.RON_92, 1L, BigDecimal.valueOf(10))
            ).andExpect(status().isNotFound());
        }

        @ParameterizedTest(name = "{1}")
        @MethodSource("invalidCreateFuelOrderRequests")
        @WithMockCustomUser
        @DisplayName("Should return Bad Request for invalid requests")
        public void shouldReturnBadRequestForInvalidRequests(CreateFuelOrderRequest request, String description) throws Exception {
            fuelOrderTestClient.createFuelOrder(request).andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> invalidCreateFuelOrderRequests() {
            AllocationRequest validAllocation = new AllocationRequest(1L, BigDecimal.valueOf(10));
            return Stream.of(
                    Arguments.of(new CreateFuelOrderRequest(null, FuelGrade.RON_92, List.of(validAllocation)), "null fuel station id"),
                    Arguments.of(new CreateFuelOrderRequest(1L, null, List.of(validAllocation)), "null fuel grade"),
                    Arguments.of(new CreateFuelOrderRequest(1L, FuelGrade.RON_92, null), "null allocations"),
                    Arguments.of(new CreateFuelOrderRequest(1L, FuelGrade.RON_92, List.of()), "empty allocations"),

                    Arguments.of(new CreateFuelOrderRequest(1L, FuelGrade.RON_92,
                            List.of(new AllocationRequest(null, BigDecimal.valueOf(10)))), "null tank id in allocation"),
                    Arguments.of(new CreateFuelOrderRequest(1L, FuelGrade.RON_92,
                            List.of(new AllocationRequest(1L, null))), "null volume in allocation"),
                    Arguments.of(new CreateFuelOrderRequest(1L, FuelGrade.RON_92,
                            List.of(new AllocationRequest(1L, BigDecimal.ZERO))), "zero volume in allocation"),
                    Arguments.of(new CreateFuelOrderRequest(1L, FuelGrade.RON_92,
                            List.of(new AllocationRequest(1L, BigDecimal.valueOf(-5)))), "negative volume in allocation"),
                    Arguments.of(new CreateFuelOrderRequest(1L, FuelGrade.RON_92,
                            List.of(new AllocationRequest(1L, BigDecimal.valueOf(-0.1)))), "negative decimal volume in allocation"),

                    Arguments.of(new CreateFuelOrderRequest(null, null, null), "all fields null")
            );
        }
    }

    @Nested
    class ConfirmFuelOrderTests {

        private FuelStationResponse testFuelStation;
        private FuelOrderResponse testFuelOrder;

        @BeforeEach
        public void beforeEach() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
            testFuelOrder = fuelOrderTestClient.createFuelOrderAndReturnResponse(singleAllocation(
                    testFuelStation.getFuelStationId(),
                    FuelGrade.RON_92,
                    tankIdByGrade(testFuelStation, FuelGrade.RON_92),
                    BigDecimal.valueOf(10)
            ));
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should confirm the fuel order")
        public void shouldConfirmFuelOrder() throws Exception {
            FuelOrderResponse confirmedFuelOrder = fuelOrderTestClient.confirmFuelOrderAndReturnResponse(testFuelOrder.getFuelOrderId());

            assertThat(confirmedFuelOrder.getStatus()).isEqualTo(FuelOrderStatus.CONFIRMED.toString());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should refill the allocated tank with the ordered volume after confirmation")
        public void shouldRefillTankAfterConfirmation() throws Exception {
            long ron92TankId = tankIdByGrade(testFuelStation, FuelGrade.RON_92);

            fuelOrderTestClient.confirmFuelOrderAndReturnResponse(testFuelOrder.getFuelOrderId());

            FuelStationResponse station = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getFuelStationId());
            FuelStationResponse.FuelTankResponse ron92Tank = station.getFuelTanks().stream()
                    .filter(t -> t.id().equals(ron92TankId))
                    .findFirst()
                    .orElseThrow();
            assertThat(ron92Tank.currentVolume()).isEqualByComparingTo(BigDecimal.valueOf(10));
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should refill each allocated tank with its own volume for multi-allocation orders")
        public void shouldRefillEachTankWithItsAllocationVolume() throws Exception {
            FuelStationResponse afterInstall = fuelStationTestClient.installFuelTankAndReturnResponse(
                    testFuelStation.getFuelStationId(),
                    new InstallFuelTankRequest(FuelGrade.RON_92, BigDecimal.valueOf(35000))
            );
            long firstRon92TankId = tankIdByGrade(afterInstall, FuelGrade.RON_92);
            long secondRon92TankId = afterInstall.getFuelTanks().stream()
                    .filter(t -> t.fuelGrade().equals(FuelGrade.RON_92.toString()))
                    .max(java.util.Comparator.comparingLong(FuelStationResponse.FuelTankResponse::id))
                    .orElseThrow()
                    .id();

            FuelOrderResponse order = fuelOrderTestClient.createFuelOrderAndReturnResponse(
                    new CreateFuelOrderRequest(
                            testFuelStation.getFuelStationId(),
                            FuelGrade.RON_92,
                            List.of(
                                    new CreateFuelOrderRequest.AllocationRequest(firstRon92TankId, BigDecimal.valueOf(300)),
                                    new CreateFuelOrderRequest.AllocationRequest(secondRon92TankId, BigDecimal.valueOf(500))
                            )
                    )
            );
            fuelOrderTestClient.confirmFuelOrderAndReturnResponse(order.getFuelOrderId());

            FuelStationResponse station = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getFuelStationId());
            FuelStationResponse.FuelTankResponse firstTank = station.getFuelTanks().stream()
                    .filter(t -> t.id().equals(firstRon92TankId))
                    .findFirst()
                    .orElseThrow();
            FuelStationResponse.FuelTankResponse secondTank = station.getFuelTanks().stream()
                    .filter(t -> t.id().equals(secondRon92TankId))
                    .findFirst()
                    .orElseThrow();
            assertThat(firstTank.currentVolume()).isEqualByComparingTo(BigDecimal.valueOf(300));
            assertThat(secondTank.currentVolume()).isEqualByComparingTo(BigDecimal.valueOf(500));
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should not affect tanks that were not in the order's allocations")
        public void shouldNotAffectOtherTanks() throws Exception {
            long ron95TankId = tankIdByGrade(testFuelStation, FuelGrade.RON_95);
            long dieselTankId = tankIdByGrade(testFuelStation, FuelGrade.DIESEL);

            fuelOrderTestClient.confirmFuelOrderAndReturnResponse(testFuelOrder.getFuelOrderId());

            FuelStationResponse station = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getFuelStationId());
            FuelStationResponse.FuelTankResponse ron95Tank = station.getFuelTanks().stream()
                    .filter(t -> t.id().equals(ron95TankId))
                    .findFirst()
                    .orElseThrow();
            FuelStationResponse.FuelTankResponse dieselTank = station.getFuelTanks().stream()
                    .filter(t -> t.id().equals(dieselTankId))
                    .findFirst()
                    .orElseThrow();
            assertThat(ron95Tank.currentVolume()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(dieselTank.currentVolume()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel order does not exist")
        public void shouldReturnNotFoundWhenFuelOrderDoesNotExist() throws Exception {
            fuelOrderTestClient.confirmFuelOrder(99999L).andExpect(status().isNotFound());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Bad Request when pricePerLiter is missing or non-positive")
        public void shouldReturnBadRequestForInvalidPrice() throws Exception {
            fuelOrderTestClient.confirmFuelOrder(testFuelOrder.getFuelOrderId(),
                    new ConfirmFuelOrderRequest(null))
                    .andExpect(status().isBadRequest());
            fuelOrderTestClient.confirmFuelOrder(testFuelOrder.getFuelOrderId(),
                    new ConfirmFuelOrderRequest(BigDecimal.ZERO))
                    .andExpect(status().isBadRequest());
            fuelOrderTestClient.confirmFuelOrder(testFuelOrder.getFuelOrderId(),
                    new ConfirmFuelOrderRequest(BigDecimal.valueOf(-1)))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class GetRecommendedPriceTests {

        private FuelStationResponse testFuelStation;
        private FuelOrderResponse testFuelOrder;

        @BeforeEach
        public void beforeEach() throws Exception {
            testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
            testFuelOrder = fuelOrderTestClient.createFuelOrderAndReturnResponse(singleAllocation(
                    testFuelStation.getFuelStationId(),
                    FuelGrade.RON_92,
                    tankIdByGrade(testFuelStation, FuelGrade.RON_92),
                    BigDecimal.valueOf(10)
            ));
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return a positive recommended price for the order's fuel grade")
        public void shouldReturnRecommendedPrice() throws Exception {
            FuelOrderRecommendedPriceResponse response = fuelOrderTestClient.getRecommendedPriceAndReturnResponse(testFuelOrder.getFuelOrderId());

            assertThat(response.pricePerLiter()).isNotNull();
            assertThat(response.pricePerLiter().signum()).isPositive();
            assertThat(response.fuelGrade()).isEqualTo(FuelGrade.RON_92.toString());
            assertThat(response.currency()).isNotNull();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel order does not exist")
        public void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
            fuelOrderTestClient.getRecommendedPrice(99999L).andExpect(status().isNotFound());
        }

    }

    @Nested
    class RejectFuelOrderTests {

        private FuelOrderResponse testFuelOrder;

        @BeforeEach
        public void beforeEach() throws Exception {
            FuelStationResponse testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
            testFuelOrder = fuelOrderTestClient.createFuelOrderAndReturnResponse(singleAllocation(
                    testFuelStation.getFuelStationId(),
                    FuelGrade.RON_92,
                    tankIdByGrade(testFuelStation, FuelGrade.RON_92),
                    BigDecimal.valueOf(10)
            ));
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should reject the fuel order")
        public void shouldRejectFuelOrder() throws Exception {
            FuelOrderResponse rejectedFuelOrder = fuelOrderTestClient.rejectFuelOrderAndReturnResponse(testFuelOrder.getFuelOrderId());

            assertThat(rejectedFuelOrder.getStatus()).isEqualTo(FuelOrderStatus.REJECTED.toString());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel order does not exist")
        public void shouldReturnNotFoundWhenFuelOrderDoesNotExist() throws Exception {
            fuelOrderTestClient.rejectFuelOrder(99999L).andExpect(status().isNotFound());
        }

    }

    @Test
    @WithMockCustomUser
    @DisplayName("Should return all fuel orders")
    public void shouldReturnAllFuelOrders() throws Exception {
        FuelStationResponse testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        fuelOrderTestClient.createFuelOrderAndReturnResponse(singleAllocation(
                testFuelStation.getFuelStationId(),
                FuelGrade.RON_92,
                tankIdByGrade(testFuelStation, FuelGrade.RON_92),
                BigDecimal.valueOf(10)
        ));
        fuelOrderTestClient.createFuelOrderAndReturnResponse(singleAllocation(
                testFuelStation.getFuelStationId(),
                FuelGrade.RON_95,
                tankIdByGrade(testFuelStation, FuelGrade.RON_95),
                BigDecimal.valueOf(10)
        ));

        List<FuelOrderResponse> fuelOrders = fuelOrderTestClient.getAllFuelOrderAndReturnResponse();
        assertThat(fuelOrders.size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Should get fuel order by id")
    public void shouldGetFuelOrderById() throws Exception {
        var testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        var testFuelOrder = fuelOrderTestClient.createFuelOrderAndReturnResponse(singleAllocation(
                testFuelStation.getFuelStationId(),
                FuelGrade.RON_92,
                tankIdByGrade(testFuelStation, FuelGrade.RON_92),
                BigDecimal.valueOf(10)
        ));

        var foundFuelOrder = fuelOrderTestClient.getFuelOrderByIdAndReturnResponse(testFuelOrder.getFuelOrderId());

        assertThat(foundFuelOrder.getFuelOrderId()).isEqualTo(testFuelOrder.getFuelOrderId());
    }

}
