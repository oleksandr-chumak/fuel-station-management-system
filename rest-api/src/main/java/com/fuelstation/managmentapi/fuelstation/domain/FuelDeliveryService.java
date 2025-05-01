package com.fuelstation.managmentapi.fuelstation.domain;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

public interface FuelDeliveryService {
    void processFuelDelivery(FuelStation fuelStation, FuelOrder fuelOrder);
}