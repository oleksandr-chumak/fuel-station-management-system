package com.fuelstation.managmentapi.fuelstation.application.exceptions;

import com.fuelstation.managmentapi.common.application.AccessDeniedException;
import com.fuelstation.managmentapi.common.domain.Actor;
import lombok.Getter;

@Getter
public class FuelStationAccessDeniedException extends AccessDeniedException {
    private final long fuelStationId;

    public FuelStationAccessDeniedException(long fuelStationId, Actor actor) {
        super(
            "User id: %d does not have the right to perform this operation for fuel station: %d".formatted(actor.id(), fuelStationId)
        );
        this.fuelStationId = fuelStationId;
    }

}
