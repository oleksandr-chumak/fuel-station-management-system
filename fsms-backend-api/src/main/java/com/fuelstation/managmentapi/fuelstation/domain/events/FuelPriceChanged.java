package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FuelPriceChanged implements DomainEvent{
    private final FuelStationEventType type = FuelStationEventType.FUEL_STATION_FUEL_PRICE_CHANGED;
    private final long fuelStationId;
    private final FuelGrade fuelGrade;
    private final BigDecimal pricePerLiter;

    public FuelPriceChanged(long fuelStationId, FuelGrade fuelGrade, BigDecimal pricePerLiter) {
        this.fuelStationId = fuelStationId;
        this.fuelGrade = fuelGrade;
        this.pricePerLiter = pricePerLiter;
    }
}
