package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationFactory;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationCreated;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class CreateFuelStation {

    @Autowired
    private FuelStationRepository fuelStationRepository;

    @Autowired FuelStationFactory fuelStationFactory;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public FuelStation process(String street, String buildingNumber, String city, String postalCode, String country ) {
        FuelStation fuelStation = fuelStationFactory.create(street, buildingNumber, city, postalCode, country);
        FuelStation savedFuelStation = fuelStationRepository.save(fuelStation);
        domainEventPublisher.publish(new FuelStationCreated(savedFuelStation.getId()));
        return savedFuelStation;
    } 
    
}
