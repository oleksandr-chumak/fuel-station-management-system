package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationFuelPriceHistoryEntity;

import java.time.Instant;
import java.util.List;

public interface FuelStationFuelPriceHistoryRepository {
    void save(FuelStationFuelPriceHistoryEntity entry);
    List<FuelStationFuelPriceHistoryEntity> findByFuelStationId(long fuelStationId);
    List<FuelStationFuelPriceHistoryEntity> findRealChanges(long fuelStationId, Instant from);
}
