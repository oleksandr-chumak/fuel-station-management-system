package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.FuelStationRepository;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Component
public class GetFuelStationById {
    
    @Autowired
    private FuelStationRepository fuelStationRepository;

    public FuelStation process(long fuelStationId) {
        return fuelStationRepository.findById(fuelStationId).orElseThrow(() -> new NoSuchElementException("Fuel station with id:" + fuelStationId + "doesn't exist"));
    }
}
