package com.fuelstation.managmentapi.fuelorder.application.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
public class GetAllFuelOrders {
    
    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    public List<FuelOrder> process() {
        return fuelOrderRepository.findAll();
    }

}
