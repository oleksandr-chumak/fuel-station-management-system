package com.fuelstation.managmentapi.fuelstation.domain;

import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Service
public class FuelDeliveryServiceImpl implements FuelDeliveryService {

    public void processFuelDelivery(FuelStation fuelStation, FuelOrder fuelOrder) {
        var performedBy = Actor.system();
        for (var allocation : fuelOrder.getAllocations()) {
            fuelStation.refillFuelTank(allocation.fuelTankId(), allocation.volume(), performedBy);
        }
    }

}
