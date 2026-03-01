package com.fuelstation.managmentapi.fuelorder.application.rest;

import com.fuelstation.managmentapi.authentication.application.UserResponse;
import com.fuelstation.managmentapi.common.application.DomainEventResponse;
import lombok.Getter;

import java.time.Instant;

@Getter
public class FuelOrderEventResponse extends DomainEventResponse {

    private final long fuelOrderId;
    private final long fuelStationId;

    public FuelOrderEventResponse(
            String type,
            Instant occurredAt,
            UserResponse performedBy,
            long fuelOrderId,
            long fuelStationId
    ) {
        super(type, occurredAt, performedBy);
        this.fuelOrderId = fuelOrderId;
        this.fuelStationId = fuelStationId;
    }

}
