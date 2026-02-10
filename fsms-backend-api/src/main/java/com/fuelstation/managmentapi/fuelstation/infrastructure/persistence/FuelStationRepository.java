package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

public interface FuelStationRepository {
  FuelStation save(FuelStation fuelStation);
  Optional<FuelStation> findById(Long id);
  List<FuelStation> findAll();
  List<FuelStation> findFuelStationsByManagerId(long managerId);
}
