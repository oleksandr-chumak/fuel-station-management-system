package com.fuelstation.managmentapi.fuelstation.application.rest.response;

import com.fuelstation.managmentapi.authentication.application.UserResponse;
import com.fuelstation.managmentapi.common.application.DomainEventResponse;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationEventType;
import lombok.Getter;

import java.time.Instant;

@Getter
public class FuelTankDecommissionedResponse extends DomainEventResponse {

    private final long fuelTankId;

    public FuelTankDecommissionedResponse(
            Instant occurredAt,
            UserResponse performedBy,
            long fuelTankId
    ) {
        super(FuelStationEventType.FUEL_TANK_DECOMMISSIONED.name(), occurredAt, performedBy);
        this.fuelTankId = fuelTankId;
    }
}
