package com.fuelstation.managmentapi.fuelstation.domain;

import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelDeliveryProcessed;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTank;

import java.math.BigDecimal;

@Service
public class FuelDeliveryServiceImpl implements FuelDeliveryService {

    public void processFuelDelivery(FuelStation fuelStation, FuelOrder fuelOrder) {

        if (fuelOrder.getStatus() != FuelOrderStatus.CONFIRMED) {
            throw new IllegalStateException("Fuel order must be in Confirmed status to process.");
        }
    
        if (fuelStation.getAvailableVolume(fuelOrder.getGrade()).compareTo(fuelOrder.getAmount()) < 0) {
            throw new IllegalStateException("Not enough tank capacity to process fuel delivery.");
        }
    
        var remainingFuel = fuelOrder.getAmount();

        for (FuelTank fuelTank : fuelStation.getFuelTanksByFuelGrade(fuelOrder.getGrade())) {
            boolean tankHasEnoughSpaceForAllRemainingFuel = fuelTank.getAvailableVolume()
                    .subtract(remainingFuel)
                    .compareTo(BigDecimal.ZERO) >= 0;
    
            if (tankHasEnoughSpaceForAllRemainingFuel) {
                fuelStation.refillFuelTank(fuelTank, remainingFuel);
                return;
            } else {
                remainingFuel = remainingFuel.subtract(fuelTank.getAvailableVolume());
                fuelStation.refillFuelTank(fuelTank, fuelTank.getAvailableVolume());
            }
        }

        fuelStation.pushDomainEvent(new FuelDeliveryProcessed(fuelOrder.getFuelOrderId()));
    }

}