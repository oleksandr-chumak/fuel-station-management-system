package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
public class GetFuelStationOrders {
   
    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    @Autowired
    private GetFuelStationById getFuelStationById;

    public List<FuelOrder> process(long fuelStationId) {
        getFuelStationById.process(fuelStationId);
        return fuelOrderRepository.findFuelOrdersByFuelStationId(fuelStationId);
    }
}
