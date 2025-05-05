package com.fuelstation.managmentapi.fuelorder.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderNotFoundException;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
public class GetFuelOrderById {
    
    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    public FuelOrder process(long fuelOrderId) {
        return fuelOrderRepository.findById(fuelOrderId)
            .orElseThrow(() -> new FuelOrderNotFoundException(fuelOrderId));
    }
}
