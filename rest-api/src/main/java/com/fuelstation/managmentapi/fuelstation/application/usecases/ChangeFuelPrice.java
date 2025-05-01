package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationRepository;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Component
public class ChangeFuelPrice {
   
    @Autowired
    private FuelStationRepository fuelStationRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public FuelStation process(long fuelStationId, FuelGrade fuelGrade, float newPrice) {
        FuelStation fuelStation = fuelStationRepository.findById(fuelStationId)
            .orElseThrow(() -> new NoSuchElementException("Fuel station with id:" + fuelStationId + "doesn't exist"));
        fuelStation.changeFuelPrice(fuelGrade, newPrice);
        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());
        return fuelStation;
    }

}
