package com.fuelstation.managmentapi.fuelorder.application.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fuelstation.managmentapi.common.WithMockCustomUser;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;
import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationResponse;
import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationTestClient;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class FuelOrderControllerTest {

    @Autowired
    private FuelOrderTestClient fuelOrderTestClient;

    @Autowired
    private FuelStationTestClient fuelStationTestClient;

    @Nested
    class CreateFuelOrderTests {

        @Test
        @WithMockCustomUser
        @DisplayName("Should create fuel order")
        public void shouldCreateFuelOrder() throws Exception {
            FuelStationResponse fuelStationResponse = fuelStationTestClient.createFuelStationAndReturnResponse();
            FuelOrderResponse fuelOrderResponse = fuelOrderTestClient.createFuelOrderAndReturnResponse(new CreateFuelOrderRequest(
                    fuelStationResponse.getId(),
                    FuelGrade.RON_92,
                    10f
            ));

            assertThat(fuelOrderResponse.getFuelGrade()).isEqualTo(FuelGrade.RON_92.toString());
            assertThat(fuelOrderResponse.getFuelStationId()).isEqualTo(fuelStationResponse.getId());
        }

        @ParameterizedTest
        @MethodSource("invalidCreateFuelOrderRequests")
        @WithMockCustomUser
        @DisplayName("Should return Bad Request for invalid requests")
        public void shouldReturnBadRequestForInvalidRequests(CreateFuelOrderRequest request) throws Exception {
            fuelOrderTestClient.createFuelOrder(request)
                    .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> invalidCreateFuelOrderRequests() {
            return Stream.of(
                    // Fuel station id validation
                    Arguments.of(new CreateFuelOrderRequest(null, FuelGrade.RON_92, 10f), "null fuel station id"),

                    // Fuel grade validation
                    Arguments.of(new CreateFuelOrderRequest(1L, null, 10f), "null fuel grade"),

                    // Amount validation
                    Arguments.of(new CreateFuelOrderRequest(1L, FuelGrade.RON_92, null), "null amount"),
                    Arguments.of(new CreateFuelOrderRequest(1L, FuelGrade.RON_92, 0f), "zero amount"),
                    Arguments.of(new CreateFuelOrderRequest(1L, FuelGrade.RON_92, -5f), "negative amount"),
                    Arguments.of(new CreateFuelOrderRequest(1L, FuelGrade.RON_92, -0.1f), "negative decimal amount"),

                    // Multiple field validation
                    Arguments.of(new CreateFuelOrderRequest(null, null, null), "all fields null"),
                    Arguments.of(new CreateFuelOrderRequest(null, null, -1f), "multiple validation failures")
            );
        }

    }

    @Nested
    class ConfirmFuelOrderTests {

        private FuelOrderResponse testFuelOrder;

        @BeforeEach
        public void beforeEach() throws Exception {
            FuelStationResponse testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();

            testFuelOrder = fuelOrderTestClient.createFuelOrderAndReturnResponse(new CreateFuelOrderRequest(
                    testFuelStation.getId(),
                    FuelGrade.RON_92,
                    10f
            ));
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should confirm the fuel order")
        public void shouldConfirmFuelOrder() throws Exception {
            FuelOrderResponse confirmedFuelOrder = fuelOrderTestClient.confirmFuelOrderAndReturnResponse(testFuelOrder.getId());

            assertThat(confirmedFuelOrder.getStatus()).isEqualTo(FuelOrderStatus.CONFIRMED.toString());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelOrderTestClient.confirmFuelOrder(99999L).andExpect(status().isNotFound());
        }

    }

    @Nested
    class RejectFuelOrderTests {

        private FuelOrderResponse testFuelOrder;

        @BeforeEach
        public void beforeEach() throws Exception {
            FuelStationResponse testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();

            testFuelOrder = fuelOrderTestClient.createFuelOrderAndReturnResponse(new CreateFuelOrderRequest(
                    testFuelStation.getId(),
                    FuelGrade.RON_92,
                    10f
            ));
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should reject the fuel order")
        public void shouldRejectFuelOrder() throws Exception {
            FuelOrderResponse rejectedFuelOrder = fuelOrderTestClient.rejectFuelOrderAndReturnResponse(testFuelOrder.getId());

            assertThat(rejectedFuelOrder.getStatus()).isEqualTo(FuelOrderStatus.REJECTED.toString());
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return Not Found when the fuel station does not exist")
        public void shouldReturnNotFoundWhenFuelStationDoesNotExist() throws Exception {
            fuelOrderTestClient.rejectFuelOrder(99999L).andExpect(status().isNotFound());
        }

    }

    @Test
    @WithMockCustomUser
    @DisplayName("Should return all fuel orders")
    public void shouldReturnAllFuelOrders() throws Exception {
        FuelStationResponse testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        fuelOrderTestClient.createFuelOrderAndReturnResponse(new CreateFuelOrderRequest(testFuelStation.getId(),FuelGrade.RON_92,10f));
        fuelOrderTestClient.createFuelOrderAndReturnResponse(new CreateFuelOrderRequest(testFuelStation.getId(),FuelGrade.RON_95,10f));

        List<FuelOrderResponse> fuelOrders = fuelOrderTestClient.getAllFuelOrderAndReturnResponse();
        assertThat(fuelOrders.size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Should get fuel order by id")
    public void shouldGetFuelOrderById() throws Exception {
        FuelStationResponse testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
        FuelOrderResponse testFuelOrder = fuelOrderTestClient.createFuelOrderAndReturnResponse(new CreateFuelOrderRequest(testFuelStation.getId(),FuelGrade.RON_92,10f));

        FuelOrderResponse foundFuelOrder = fuelOrderTestClient.getFuelOrderByIdAndReturnResponse(testFuelOrder.getId());

        assertThat(foundFuelOrder.getId()).isEqualTo(testFuelOrder.getId());
    }

}
