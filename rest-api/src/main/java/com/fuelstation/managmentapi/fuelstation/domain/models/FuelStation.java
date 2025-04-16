package com.fuelstation.managmentapi.fuelstation.domain.models;

import java.time.LocalDate;
import java.util.List;

public record FuelStation(
    Long id,
    FuelStationAddress address,
    List<FuelTank> fuelTanks,
    List<FuelPrice> fuelPrices,
    List<Long> assignedManagersIds,
    FuelStationStatus status,
    LocalDate createdAt
) {}