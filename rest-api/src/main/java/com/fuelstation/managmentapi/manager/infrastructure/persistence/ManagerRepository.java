package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.manager.domain.Manager;

public interface ManagerRepository {
   public Manager save(Manager manager);
   public Optional<Manager> findById(long id);   
   public List<Manager> findAll();
   public List<Manager> findManagersAssignedToFuelStation(long fuelStationId);
   public List<FuelStation> findManagerFuelStation(long managerId);
}
