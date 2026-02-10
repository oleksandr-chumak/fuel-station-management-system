package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
@AllArgsConstructor
public class GetAllFuelStations {

    private final FuelStationRepository fuelStationRepository;

    public List<FuelStation> process() {
        return fuelStationRepository.findAll(); 
    }
    
}
