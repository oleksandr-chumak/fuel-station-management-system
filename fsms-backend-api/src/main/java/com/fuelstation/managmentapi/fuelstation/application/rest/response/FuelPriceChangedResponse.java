package com.fuelstation.managmentapi.fuelstation.application.rest.response;

import com.fuelstation.managmentapi.authentication.application.UserResponse;
import com.fuelstation.managmentapi.common.application.DomainEventResponse;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationEventType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class FuelPriceChangedResponse extends DomainEventResponse {

    private final FuelGrade fuelGrade;
    private final BigDecimal pricePerLiter;

    public FuelPriceChangedResponse(
            Instant occurredAt,
            UserResponse performedBy,
            FuelGrade fuelGrade,
            BigDecimal pricePerLiter
    ) {
        super(FuelStationEventType.FUEL_STATION_FUEL_PRICE_CHANGED.name(), occurredAt, performedBy);
        this.fuelGrade = fuelGrade;
        this.pricePerLiter = pricePerLiter;
    }

}
