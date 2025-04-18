package com.fuelstation.managmentapi.fuelorder.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderService;

@Component
public class RejectFuelOrder {
    
    @Autowired
    private FuelOrderService fuelOrderService;
    
    public FuelOrder process(long fuelOrderId) {
        return fuelOrderService.rejectFuelOrder(fuelOrderId);
    }
}