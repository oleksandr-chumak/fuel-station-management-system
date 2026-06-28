package com.fuelstation.managmentapi.fuelorder.application.rest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

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
    private BigDecimal volume;
    private List<AllocationResponse> allocations;
    private String status;
    private OffsetDateTime createdAt;

    public static FuelOrderResponse fromDomain(FuelOrder fuelOrder) {
        FuelOrderResponse response = new FuelOrderResponse();
        response.setFuelOrderId(fuelOrder.getFuelOrderId());
        response.setFuelStationId(fuelOrder.getFuelStationId());
        response.setFuelGrade(fuelOrder.getGrade().toString());
        response.setVolume(fuelOrder.getVolume());
        response.setAllocations(fuelOrder.getAllocations().stream()
                .map(a -> new AllocationResponse(a.fuelTankId(), a.volume()))
                .toList());
        response.setStatus(fuelOrder.getStatus().toString());
        response.setCreatedAt(fuelOrder.getCreatedAt());
        return response;
    }

    public record AllocationResponse(Long fuelTankId, BigDecimal volume) {
    }
}
