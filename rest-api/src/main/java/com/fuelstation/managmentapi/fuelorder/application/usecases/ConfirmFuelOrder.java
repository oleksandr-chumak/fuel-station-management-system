package com.fuelstation.managmentapi.fuelorder.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderService;

@Component
public class ConfirmFuelOrder {
    
    @Autowired
    private FuelOrderService fuelOrderService;
    
    public FuelOrder process(Long fuelOrderId) {
        return fuelOrderService.confirmFuelOrder(fuelOrderId);
    }
}