package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.application.exceptions.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class GetFuelStationById {
    
    private final FuelStationRepository fuelStationRepository;

    public GetFuelStationById(FuelStationRepository fuelStationRepository) {
        this.fuelStationRepository = fuelStationRepository;
    }

    public FuelStation process(long fuelStationId) {
        return fuelStationRepository.findById(fuelStationId)
            .orElseThrow(() -> new FuelStationNotFoundException(fuelStationId));
    }
}
