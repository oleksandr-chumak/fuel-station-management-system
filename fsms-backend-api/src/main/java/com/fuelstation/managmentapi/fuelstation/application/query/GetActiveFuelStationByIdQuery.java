package com.fuelstation.managmentapi.fuelstation.application.query;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationDeactivatedException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GetActiveFuelStationByIdQuery {

    private final GetFuelStationByIdQuery getFuelStationByIdQuery;

    public FuelStation process(long fuelStationId, Actor actor) {
        FuelStation station = getFuelStationByIdQuery.process(fuelStationId, actor);
        if (station.deactivated()) {
            throw new FuelStationDeactivatedException(fuelStationId);
        }
        return station;
    }
}
