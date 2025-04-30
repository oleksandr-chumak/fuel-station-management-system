package com.fuelstation.managmentapi.fuelorder.application.usecases;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
public class RejectFuelOrder {
    
    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;
    
    public FuelOrder process(long fuelOrderId) {
        FuelOrder fuelOrder = fuelOrderRepository.findById(fuelOrderId)
            .orElseThrow(() -> new NoSuchElementException("Fuel order with id:" + fuelOrderId + "doesn't exist"));
        fuelOrder.reject();
        fuelOrderRepository.save(fuelOrder);
        domainEventPublisher.publishAll(fuelOrder.getDomainEvents());
        return fuelOrder;
    }
}