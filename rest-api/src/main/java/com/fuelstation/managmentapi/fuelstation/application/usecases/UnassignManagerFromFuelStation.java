package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationRepository;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
public class UnassignManagerFromFuelStation {
    
    @Autowired
    private FuelStationRepository fuelStationRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;


    public FuelStation process(long fuelStationId, long managerId) {
        FuelStation fuelStation = fuelStationRepository.findById(fuelStationId)
            .orElseThrow(() -> new NoSuchElementException("Fuel station with id:" + fuelStationId + "doesn't exist"));
        Manager manager = managerRepository.findById(managerId)
            .orElseThrow(() -> new NoSuchElementException("Manager with id:" + managerId + "doesn't exist"));
        fuelStation.unassignManager(manager.getId());
        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());
        return fuelStation;
    }
}
