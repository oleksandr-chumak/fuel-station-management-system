package com.fuelstation.managmentapi.fuelstation.domain;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

public interface FuelStationRepository {
  public FuelStation save(FuelStation fuelStation);
  public Optional<FuelStation> findById(Long id); 
  public List<FuelStation> findAll();      
  public void deleteAll();  
}
