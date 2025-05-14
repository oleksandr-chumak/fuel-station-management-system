package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.application.usecases.GetFuelOrderById;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelstation.domain.FuelDeliveryService;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class ProcessFuelDelivery {

    @Autowired
    private FuelStationRepository fuelStationRepository;

    @Autowired
    private FuelDeliveryService fuelDeliveryService;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private GetFuelStationById getFuelStationById;
    
    @Autowired
    private GetFuelOrderById getFuelOrderById;

    public FuelStation process(long fuelOrderId) {
        FuelOrder fuelOrder = getFuelOrderById.process(fuelOrderId);
        FuelStation fuelStation = getFuelStationById.process(fuelOrder.getFuelStationId());

        fuelDeliveryService.processFuelDelivery(fuelStation, fuelOrder);

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }
}
