package com.fuelstation.managmentapi.fuelorder.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

@Component
public class DomainFuelOrderFactory implements FuelOrderFactory {

    @Override
    public FuelOrder create(Long fuelStationId, FuelGrade fuelGrade, BigDecimal amount) {
        return new FuelOrder(null, FuelOrderStatus.PENDING, fuelGrade, amount, fuelStationId, OffsetDateTime.now());
    }
}