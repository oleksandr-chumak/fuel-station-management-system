package com.fuelstation.managmentapi.fuelorder.application.usecases;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
public class GetAllFuelOrders {
    
    private final FuelOrderRepository fuelOrderRepository;

    public GetAllFuelOrders(FuelOrderRepository fuelOrderRepository) {
        this.fuelOrderRepository = fuelOrderRepository;
    }

    public List<FuelOrder> process() {
        return fuelOrderRepository.findAll();
    }

}
