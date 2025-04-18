package com.fuelstation.managmentapi.fuelorder.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderService;

@Component
public class CreateFuelOrder {
    
    @Autowired
    private FuelOrderService fuelOrderService;
    
    public FuelOrder process(Long gasStationId, String fuelGrade, float amount) {
        return fuelOrderService.createFuelOrder(gasStationId, getFuelGrade(fuelGrade), amount);
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
