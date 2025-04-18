package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderRepository;

@Component
public class GetFuelStationOrders {
   
    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    public List<FuelOrder> process(Long fuelStationId) {
        return fuelOrderRepository.findFuelOrdersByFuelStationId(fuelStationId);
    }
}
