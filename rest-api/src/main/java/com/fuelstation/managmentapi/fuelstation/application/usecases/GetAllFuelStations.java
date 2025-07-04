package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class GetAllFuelStations {

    private final FuelStationRepository fuelStationRepository;

    public GetAllFuelStations(FuelStationRepository fuelStationRepository) {
        this.fuelStationRepository = fuelStationRepository;
    }

    public List<FuelStation> process() {
        return fuelStationRepository.findAll(); 
    }
    
}
