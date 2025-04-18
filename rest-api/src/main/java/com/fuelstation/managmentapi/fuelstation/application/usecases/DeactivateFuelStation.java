package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.FuelStationService;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Component
public class DeactivateFuelStation {
    
    @Autowired
    private FuelStationService fuelStationService;
    
    public FuelStation process(long fuelStationId) {
        return fuelStationService.deactivateFuelStation(fuelStationId); 
    }
}
