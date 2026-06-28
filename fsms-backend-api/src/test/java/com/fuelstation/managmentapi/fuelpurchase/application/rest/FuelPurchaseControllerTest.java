package com.fuelstation.managmentapi.fuelpurchase.application.rest;

import com.fuelstation.managmentapi.common.WithMockCustomUser;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.application.rest.CreateFuelOrderRequest;
import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderTestClient;
import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationResponse;
import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationTestClient;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class FuelPurchaseControllerTest {

    @Autowired
    private FuelPurchaseTestClient fuelPurchaseTestClient;

    @Autowired
    private FuelStationTestClient fuelStationTestClient;

    @Autowired
    private FuelOrderTestClient fuelOrderTestClient;

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
                List.of(new CreateFuelOrderRequest.AllocationRequest(tankId, volume))
        );
    }

    @Nested
    class GetFuelPurchasesByStation {

        @Test
        @WithMockCustomUser
        @DisplayName("Should return purchase after fuel order is confirmed")
        void shouldReturnPurchaseAfterOrderConfirmed() throws Exception {
            var station = fuelStationTestClient.createFuelStationAndReturnResponse();
            var order = fuelOrderTestClient.createFuelOrderAndReturnResponse(singleAllocation(
                    station.getFuelStationId(),
                    FuelGrade.RON_95,
                    tankIdByGrade(station, FuelGrade.RON_95),
                    BigDecimal.valueOf(100)
            ));
            fuelOrderTestClient.confirmFuelOrder(order.getFuelOrderId())
                    .andExpect(status().isOk());

            var purchases = fuelPurchaseTestClient.getPurchasesByStationAndReturnResponse(station.getFuelStationId());

            assertThat(purchases).hasSize(1);
            assertThat(purchases.getFirst().fuelOrderId()).isEqualTo(order.getFuelOrderId());
            assertThat(purchases.getFirst().fuelStationId()).isEqualTo(station.getFuelStationId());
            assertThat(purchases.getFirst().fuelGrade()).isEqualTo(FuelGrade.RON_95.toString());
            assertThat(purchases.getFirst().amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
            assertThat(purchases.getFirst().pricePerLiter()).isPositive();
            assertThat(purchases.getFirst().totalPrice()).isPositive();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return empty list when station has no confirmed orders")
        void shouldReturnEmptyListWhenNoConfirmedOrders() throws Exception {
            var station = fuelStationTestClient.createFuelStationAndReturnResponse();

            var purchases = fuelPurchaseTestClient.getPurchasesByStationAndReturnResponse(station.getFuelStationId());

            assertThat(purchases).isEmpty();
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should return multiple purchases for multiple confirmed orders")
        void shouldReturnMultiplePurchases() throws Exception {
            var station = fuelStationTestClient.createFuelStationAndReturnResponse();

            for (var grade : List.of(FuelGrade.RON_95, FuelGrade.DIESEL)) {
                var order = fuelOrderTestClient.createFuelOrderAndReturnResponse(singleAllocation(
                        station.getFuelStationId(),
                        grade,
                        tankIdByGrade(station, grade),
                        BigDecimal.valueOf(50)
                ));
                fuelOrderTestClient.confirmFuelOrder(order.getFuelOrderId());
            }

            var purchases = fuelPurchaseTestClient.getPurchasesByStationAndReturnResponse(station.getFuelStationId());

            assertThat(purchases).hasSize(2);
        }

        @Test
        @WithMockCustomUser
        @DisplayName("Should not create purchase when order is rejected")
        void shouldNotCreatePurchaseWhenOrderRejected() throws Exception {
            var station = fuelStationTestClient.createFuelStationAndReturnResponse();
            var order = fuelOrderTestClient.createFuelOrderAndReturnResponse(singleAllocation(
                    station.getFuelStationId(),
                    FuelGrade.RON_95,
                    tankIdByGrade(station, FuelGrade.RON_95),
                    BigDecimal.valueOf(100)
            ));
            fuelOrderTestClient.rejectFuelOrder(order.getFuelOrderId())
                    .andExpect(status().isOk());

            var purchases = fuelPurchaseTestClient.getPurchasesByStationAndReturnResponse(station.getFuelStationId());

            assertThat(purchases).isEmpty();
        }
    }
}
