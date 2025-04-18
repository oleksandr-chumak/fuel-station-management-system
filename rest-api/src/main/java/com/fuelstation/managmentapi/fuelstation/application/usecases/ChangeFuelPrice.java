package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationService;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Component
public class ChangeFuelPrice {
   
    @Autowired
    private FuelStationService fuelStationService;

    public FuelStation process(long fuelStationId, String fuelGrade, float newPrice) {
        return fuelStationService.changeFuelPrice(fuelStationId, getFuelGrade(fuelGrade), newPrice);
    }

    // fuelGrade can be ron-92, ron-95, diesel
    private FuelGrade getFuelGrade(String fuelGrade) {
        switch (fuelGrade) {
            case "ron-92":
                return FuelGrade.RON_92;
            case "ron-95":
                return FuelGrade.RON_95;
            case "diesel":
                return FuelGrade.Diesel;
            default:
                throw new IllegalArgumentException("Fuel grade can only be: ron-92, ron-95, diesel");
        }
    }
}
