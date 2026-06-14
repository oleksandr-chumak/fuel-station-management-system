package com.fuelstation.managmentapi.fuelstation.application.query;

import java.util.List;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelstation.application.exceptions.FuelStationAccessDeniedException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
@AllArgsConstructor
public class ListFuelStationManagersQuery {

    private final ManagerRepository managerRepository;
    private final GetActiveFuelStationByIdQuery getActiveFuelStationByIdQuery;

    @Transactional
    public List<Manager> process(long fuelStationId, Actor performedBy) {
        var fuelStation = getActiveFuelStationByIdQuery.process(fuelStationId, performedBy);
        checkAccess(fuelStation, performedBy);
        List<Long> assignedManagerIds = fuelStation.getAssignedManagersIds();
        return managerRepository.findByIds(assignedManagerIds);
    }

    private void checkAccess(FuelStation fuelStation, Actor actor) {
        var hasAccess = actor.isSystem() || actor.isAdmin()
                || fuelStation.isManagerAssigned(actor.id());
        if (!hasAccess) {
            throw new FuelStationAccessDeniedException(fuelStation.getFuelStationId(), actor);
        }
    }
}
