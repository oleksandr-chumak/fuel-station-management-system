package com.fuelstation.managmentapi.fuelorder.domain;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationRepository;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

public class DomainFuelOrderFactory implements FuelOrderFactory {
    @Autowired
    private FuelStationRepository fuelStationRepository;

    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    @Override
    public FuelOrder create(Long fuelStationId, FuelGrade fuelGrade, float amount) {
        FuelStation fuelStation = fuelStationRepository.findById(fuelStationId).orElseThrow(() -> new NoSuchElementException("Fuel station with id:" + fuelStationId + "doesn't exist"));

        float availableVolume = fuelStation.getAvailableVolume(fuelGrade);
        float pendingAmount = fuelOrderRepository.getUnconfirmedFuelAmount(fuelStationId, fuelGrade);
        float allowedAmount = availableVolume - pendingAmount;

        if(amount > allowedAmount) {
            throw new IllegalArgumentException("Ordered amount (" + amount + "L) exceeds available tank space (" + availableVolume + "L) minus pending orders (" + pendingAmount + "L) for " + fuelGrade + " at station ID " + fuelStationId + ". Max allowed: " + allowedAmount + "L.");
        }

        return new FuelOrder(null, FuelOrderStatus.Pending, fuelGrade, amount, fuelStationId, LocalDate.now()); 
    }
}