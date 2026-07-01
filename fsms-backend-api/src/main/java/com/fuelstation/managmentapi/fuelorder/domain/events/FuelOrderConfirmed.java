package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public final class FuelOrderConfirmed extends FuelOrderEvent {

    private final BigDecimal pricePerLiter;
    private final CurrencyCode currency;

    public FuelOrderConfirmed(
        long fuelOrderId,
        long fuelStationId,
        BigDecimal pricePerLiter,
        CurrencyCode currency,
        Actor performedBy
    ) {
        super(FuelOrderEventType.FUEL_ORDER_CONFIRMED, fuelOrderId, fuelStationId, performedBy);
        this.pricePerLiter = pricePerLiter;
        this.currency = currency;
    }

    public FuelOrderConfirmed(
        long fuelOrderId,
        long fuelStationId,
        BigDecimal pricePerLiter,
        CurrencyCode currency,
        Actor performedBy,
        Instant occurredAt
    ) {
        super(FuelOrderEventType.FUEL_ORDER_CONFIRMED, fuelOrderId, fuelStationId, performedBy, occurredAt);
        this.pricePerLiter = pricePerLiter;
        this.currency = currency;
    }

}
