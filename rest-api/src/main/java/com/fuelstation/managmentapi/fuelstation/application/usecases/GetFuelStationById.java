package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class GetFuelStationById {
    
    @Autowired
    private FuelStationRepository fuelStationRepository;

    public FuelStation process(long fuelStationId) {
        return fuelStationRepository.findById(fuelStationId)
            .orElseThrow(() -> new FuelStationNotFoundException(fuelStationId));
    }
}
