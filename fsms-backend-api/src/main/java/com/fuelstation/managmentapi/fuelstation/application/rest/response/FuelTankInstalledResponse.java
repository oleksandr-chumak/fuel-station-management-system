package com.fuelstation.managmentapi.fuelstation.application.rest.response;

import com.fuelstation.managmentapi.authentication.application.UserResponse;
import com.fuelstation.managmentapi.common.application.DomainEventResponse;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationEventType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class FuelTankInstalledResponse extends DomainEventResponse {

    private final long fuelTankId;
    private final FuelGrade fuelGrade;
    private final BigDecimal maxCapacity;

    public FuelTankInstalledResponse(
            Instant occurredAt,
            UserResponse performedBy,
            long fuelTankId,
            FuelGrade fuelGrade,
            BigDecimal maxCapacity
    ) {
        super(FuelStationEventType.FUEL_TANK_INSTALLED.name(), occurredAt, performedBy);
        this.fuelTankId = fuelTankId;
        this.fuelGrade = fuelGrade;
        this.maxCapacity = maxCapacity;
    }
}
