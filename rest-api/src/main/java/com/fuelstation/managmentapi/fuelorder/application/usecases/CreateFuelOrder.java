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
    
    public FuelOrder process(Long fuelStationId, String fuelGrade, Float amount) {
        requireNonNull(fuelStationId, "Fuel station ID cannot be null");
        requireNonNull(fuelGrade, "Fuel grade cannot be null");
        requireNonNull(amount, "Amount cannot be null");
        
        return fuelOrderService.createFuelOrder(fuelStationId.longValue(), getFuelGrade(fuelGrade), amount.floatValue());
    }

    /**
     * TODO make it util
     * Utility method to check for null and throw IllegalArgumentException
     */
    private <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        return obj;
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
