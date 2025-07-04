package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
public class GetFuelStationOrders {
   
    private final FuelOrderRepository fuelOrderRepository;

    private final GetFuelStationById getFuelStationById;

    public GetFuelStationOrders(FuelOrderRepository fuelOrderRepository, GetFuelStationById getFuelStationById) {
        this.fuelOrderRepository = fuelOrderRepository;
        this.getFuelStationById = getFuelStationById;
    }

    @Transactional
    public List<FuelOrder> process(long fuelStationId) {
        getFuelStationById.process(fuelStationId);
        return fuelOrderRepository.findFuelOrdersByFuelStationId(fuelStationId);
    }
}
