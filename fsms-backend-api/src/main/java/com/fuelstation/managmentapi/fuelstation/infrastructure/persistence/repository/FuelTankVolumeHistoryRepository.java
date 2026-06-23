package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTankVolumeHistory;

import java.util.List;

public interface FuelTankVolumeHistoryRepository {
    FuelTankVolumeHistory save(FuelTankVolumeHistory history);
    List<FuelTankVolumeHistory> findByFuelStationId(long fuelStationId);
}
