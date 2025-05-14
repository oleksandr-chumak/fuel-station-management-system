package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;
import com.fuelstation.managmentapi.manager.application.usecases.GetManagerById;
import com.fuelstation.managmentapi.manager.domain.Manager;

@Component
public class UnassignManagerFromFuelStation {
    
    @Autowired
    private FuelStationRepository fuelStationRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private GetFuelStationById getFuelStationById;

    @Autowired
    private GetManagerById getManagerById;


    public FuelStation process(long fuelStationId, long managerId) {
        FuelStation fuelStation = getFuelStationById.process(fuelStationId);
        Manager manager = getManagerById.process(managerId);
        fuelStation.unassignManager(manager.getId());
        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());
        return fuelStation;
    }
}
