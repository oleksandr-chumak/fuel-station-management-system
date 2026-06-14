package com.fuelstation.managmentapi.fuelstation.application.query;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelstation.application.exceptions.FuelStationAccessDeniedException;
import com.fuelstation.managmentapi.fuelstation.application.exceptions.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Component
@AllArgsConstructor
public class GetFuelStationByIdQuery {

    private final FuelStationRepository fuelStationRepository;

    public FuelStation process(long fuelStationId, Actor actor) {
        var fuelStation = fuelStationRepository.findById(fuelStationId)
            .orElseThrow(() -> new FuelStationNotFoundException(fuelStationId));

        checkAccess(fuelStation, actor);

        return fuelStation;
    }

    private void checkAccess(FuelStation fuelStation, Actor actor) {
        if(actor.isSystem() || actor.isAdmin()) {
            return;
        }
        if(actor.isManager() && !fuelStation.isManagerAssigned(actor.id())) {
            throw new FuelStationAccessDeniedException(fuelStation.getFuelStationId(), actor);
        }
    }

}
