package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.FuelStationService;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Component
public class AssignManagerToFuelStation {
    
    @Autowired
    private FuelStationService fuelStationService;

    public FuelStation process(Long fuelStationId, Long mangerId) {
        return fuelStationService.assignManager(0, 0);
    }

}