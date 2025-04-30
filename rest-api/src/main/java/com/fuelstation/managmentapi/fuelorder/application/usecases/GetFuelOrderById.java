package com.fuelstation.managmentapi.fuelorder.application.usecases;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
public class GetFuelOrderById {
    
    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    public FuelOrder process(long fuelOrderId) {
        return fuelOrderRepository.findById(fuelOrderId).orElseThrow(() -> new NoSuchElementException("Fuel order with id:" + fuelOrderId + "doesn't exist"));
    }
}
