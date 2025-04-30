package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;

public interface FuelOrderRepository {
    public FuelOrder save(FuelOrder fuelOrder);
    public Optional<FuelOrder> findById(long id);
    public List<FuelOrder> findAll();
    public List<FuelOrder> findFuelOrdersByFuelStationId(Long fuelStationId);
    public float getUnconfirmedFuelAmount(Long gasStationId, FuelGrade fuelGrade);
    public void deleteAll();
}
