package com.fuelstation.managmentapi.fuelorder.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
public class ConfirmFuelOrder {

    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private GetFuelOrderById getFuelOrderById;
    
    public FuelOrder process(long fuelOrderId) {
        FuelOrder fuelOrder = getFuelOrderById.process(fuelOrderId);
        fuelOrder.confirm();
        fuelOrderRepository.save(fuelOrder);
        domainEventPublisher.publishAll(fuelOrder.getDomainEvents());
        return fuelOrder;
    }
}