package com.fuelstation.managmentapi.fuelorder.domain;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

public interface FuelOrderService {
    public FuelOrder createFuelOrder(Long gasStationId, FuelGrade fuelGrade, float amount);
    public FuelOrder confirmFuelOrder(Long fuelOrderId);
    public FuelOrder rejectFuelOrder(Long fuelOrderId);
}
