package com.fuelstation.managmentapi.fuelorder.application.rest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelOrderResponse {
    private Long fuelOrderId;
    private Long fuelStationId;
    private String fuelGrade;
    private BigDecimal amount;
    private String status;
    private OffsetDateTime createdAt;

    public static FuelOrderResponse fromDomain(FuelOrder fuelOrder) {
        FuelOrderResponse response = new FuelOrderResponse();
        response.setFuelOrderId(fuelOrder.getFuelOrderId());
        response.setFuelStationId(fuelOrder.getFuelStationId());
        response.setFuelGrade(fuelOrder.getGrade().toString());
        response.setAmount(fuelOrder.getAmount());
        response.setStatus(fuelOrder.getStatus().toString());
        response.setCreatedAt(fuelOrder.getCreatedAt());
        return response;
    }
}