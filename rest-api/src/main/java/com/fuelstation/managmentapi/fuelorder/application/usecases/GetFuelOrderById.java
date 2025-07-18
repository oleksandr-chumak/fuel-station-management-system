package com.fuelstation.managmentapi.fuelorder.application.usecases;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.application.exceptions.FuelOrderNotFoundException;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
public class GetFuelOrderById {
    
    private final FuelOrderRepository fuelOrderRepository;

    public GetFuelOrderById(FuelOrderRepository fuelOrderRepository) {
        this.fuelOrderRepository = fuelOrderRepository;
    }

    public FuelOrder process(long fuelOrderId) {
        return fuelOrderRepository.findById(fuelOrderId)
            .orElseThrow(() -> new FuelOrderNotFoundException(fuelOrderId));
    }
}
