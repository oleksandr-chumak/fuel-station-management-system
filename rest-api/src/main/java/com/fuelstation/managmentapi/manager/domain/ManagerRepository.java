package com.fuelstation.managmentapi.manager.domain;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository {
   public Manager save(Manager manager);
   public Optional<Manager> findById(long id);   
   public List<Manager> findAll();
   public List<Manager> findManagersAssignedToFuelStation(Long fuelStationId);
}
