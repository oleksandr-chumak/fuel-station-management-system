package com.fuelstation.managmentapi.fuelorder.domain;

import java.util.Optional;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

public interface FuelOrderRepository {
    public FuelOrder save(FuelOrder fuelOrder);
    public Optional<FuelOrder> findById(long id);
    public float getUnconfirmedFuelAmount(Long gasStationId, FuelGrade fuelGrade);
    public void deleteAll();
}
