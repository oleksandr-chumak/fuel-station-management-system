package com.fuelstation.managmentapi.fuelorder.application.rest;

import java.time.LocalDate;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelOrderResponse {
    private Long id;
    private Long fuelStationId;
    private String fuelGrade;
    private float amount;
    private String status;
    private LocalDate createdAt;

    public static FuelOrderResponse fromDomain(FuelOrder fuelOrder) {
        FuelOrderResponse response = new FuelOrderResponse();
        response.setId(fuelOrder.getId());
        response.setFuelStationId(fuelOrder.getFuelStationId());
        response.setFuelGrade(fuelOrder.getGrade().toString());
        response.setAmount(fuelOrder.getAmount());
        response.setStatus(fuelOrder.getStatus().toString());
        response.setCreatedAt(fuelOrder.getCreatedAt());
        return response;
    }
}