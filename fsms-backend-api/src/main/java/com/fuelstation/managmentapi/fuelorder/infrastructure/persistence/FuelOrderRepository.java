package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;

public interface FuelOrderRepository {
     FuelOrder save(FuelOrder fuelOrder);
     Optional<FuelOrder> findById(long id);
     List<FuelOrder> findAll();
     List<FuelOrder> findFuelOrdersByFuelStationId(long fuelStationId);
     List<FuelOrder> findPendingByFuelTankIdsAndGrade(List<Long> fuelTankIds, FuelGrade fuelGrade);
     List<FuelOrder> findPendingByFuelTankId(long fuelTankId);
}
