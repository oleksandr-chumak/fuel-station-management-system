package com.fuelstation.managmentapi.fuelorder.domain;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

public interface FuelOrderService {
    public FuelOrder createFuelOrder(long gasStationId, FuelGrade fuelGrade, float amount);
    public FuelOrder confirmFuelOrder(long fuelOrderId);
    public FuelOrder rejectFuelOrder(long fuelOrderId);
}
