package com.fuelstation.managmentapi.fuelstation.application.rest.response;

import com.fuelstation.managmentapi.authentication.application.UserResponse;
import com.fuelstation.managmentapi.common.application.DomainEventResponse;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationEventType;
import com.fuelstation.managmentapi.manager.application.rest.ManagerResponse;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ManagerUnassignedFromFuelStationResponse extends DomainEventResponse {

    private final ManagerResponse manager;

    public ManagerUnassignedFromFuelStationResponse(
            Instant occurredAt,
            UserResponse performedBy,
            ManagerResponse manager
    ) {
        super(FuelStationEventType.MANAGER_UNASSIGNED_FROM_FUEL_STATION.name(), occurredAt, performedBy);
        this.manager = manager;
    }

}
