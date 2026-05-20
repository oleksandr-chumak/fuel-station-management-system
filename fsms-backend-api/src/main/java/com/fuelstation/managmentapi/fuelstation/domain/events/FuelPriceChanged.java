package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class FuelPriceChanged extends FuelStationEvent {
    private final FuelGrade fuelGrade;
    private final BigDecimal pricePerLiter;
    private final CurrencyCode currency;

    public FuelPriceChanged(long fuelStationId, FuelGrade fuelGrade, BigDecimal pricePerLiter, CurrencyCode currency, Actor performedBy) {
        super(FuelStationEventType.FUEL_STATION_FUEL_PRICE_CHANGED, fuelStationId, performedBy);
        this.fuelGrade = fuelGrade;
        this.pricePerLiter = pricePerLiter;
        this.currency = currency;
    }

    public FuelPriceChanged(long fuelStationId, FuelGrade fuelGrade, BigDecimal pricePerLiter, CurrencyCode currency, Actor performedBy, Instant occurredAt) {
        super(FuelStationEventType.FUEL_STATION_FUEL_PRICE_CHANGED, fuelStationId, performedBy, occurredAt);
        this.fuelGrade = fuelGrade;
        this.pricePerLiter = pricePerLiter;
        this.currency = currency;
    }
}
