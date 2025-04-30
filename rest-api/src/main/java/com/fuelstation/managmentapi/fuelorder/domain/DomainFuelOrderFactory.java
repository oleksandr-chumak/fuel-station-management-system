package com.fuelstation.managmentapi.fuelorder.domain;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

@Component
public class DomainFuelOrderFactory implements FuelOrderFactory {

    @Override
    public FuelOrder create(Long fuelStationId, FuelGrade fuelGrade, float amount) {
        return new FuelOrder(null, FuelOrderStatus.PENDING, fuelGrade, amount, fuelStationId, LocalDate.now()); 
    }
}