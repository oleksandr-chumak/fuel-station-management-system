package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class GetAllFuelStations {

    @Autowired
    private FuelStationRepository fuelStationRepository;

    public List<FuelStation> process() {
        return fuelStationRepository.findAll(); 
    }
    
}
