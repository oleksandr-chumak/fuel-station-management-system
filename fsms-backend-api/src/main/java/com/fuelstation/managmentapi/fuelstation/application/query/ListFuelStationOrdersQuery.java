package com.fuelstation.managmentapi.fuelstation.application.query;

import java.util.List;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelstation.application.exceptions.FuelStationAccessDeniedException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
@AllArgsConstructor
public class ListFuelStationOrdersQuery {

    private final FuelOrderRepository fuelOrderRepository;
    private final GetActiveFuelStationByIdQuery getActiveFuelStationByIdQuery;

    @Transactional
    public List<FuelOrder> process(long fuelStationId, Actor performedBy) {
        var fuelStation = getActiveFuelStationByIdQuery.process(fuelStationId, performedBy);
        checkAccess(fuelStation, performedBy);
        return fuelOrderRepository.findFuelOrdersByFuelStationId(fuelStationId);
    }

    private void checkAccess(FuelStation fuelStation, Actor actor) {
        var hasAccess = actor.isSystem() || actor.isAdmin()
                || fuelStation.isManagerAssigned(actor.id());
        if (!hasAccess) {
            throw new FuelStationAccessDeniedException(fuelStation.getFuelStationId(), actor);
        }
    }
}
