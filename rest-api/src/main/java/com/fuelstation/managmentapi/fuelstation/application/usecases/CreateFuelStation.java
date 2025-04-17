package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.FuelStationService;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Component
public class CreateFuelStation {

    @Autowired
    private FuelStationService fuelStationService;

    public FuelStation process(String street, String buildingNumber, String city, String postalCode, String country ) {
        return fuelStationService.createFuelStation(street, buildingNumber, city, postalCode, country);
    } 
    
}
