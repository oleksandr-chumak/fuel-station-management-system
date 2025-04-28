package com.fuelstation.managmentapi.manager.domain;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

public interface ManagerRepository {
   public Manager save(Manager manager);
   public Optional<Manager> findById(long id);   
   public List<Manager> findAll();
   public List<Manager> findManagersAssignedToFuelStation(long fuelStationId);
   public List<FuelStation> findManagerFuelStation(long managerId);
}
