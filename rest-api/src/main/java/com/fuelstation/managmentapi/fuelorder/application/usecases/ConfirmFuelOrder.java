package com.fuelstation.managmentapi.fuelorder.application.usecases;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
public class ConfirmFuelOrder {

    private final FuelOrderRepository fuelOrderRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final GetFuelOrderById getFuelOrderById;

    public ConfirmFuelOrder(FuelOrderRepository fuelOrderRepository, DomainEventPublisher domainEventPublisher, GetFuelOrderById getFuelOrderById) {
        this.fuelOrderRepository = fuelOrderRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.getFuelOrderById = getFuelOrderById;
    }

    @Transactional
    public FuelOrder process(long fuelOrderId) {
        FuelOrder fuelOrder = getFuelOrderById.process(fuelOrderId);

        fuelOrder.confirm();

        fuelOrderRepository.save(fuelOrder);
        domainEventPublisher.publishAll(fuelOrder.getDomainEvents());

        return fuelOrder;
    }

}