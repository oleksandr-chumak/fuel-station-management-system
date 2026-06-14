package com.fuelstation.managmentapi.fuelorder.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelorder.application.query.GetFuelOrderByIdQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
@AllArgsConstructor
public class RejectFuelOrder {

    private final FuelOrderRepository fuelOrderRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final GetFuelOrderByIdQuery getFuelOrderByIdQuery;

    @Transactional
    public FuelOrder process(long fuelOrderId, Actor performedBy) {
        FuelOrder fuelOrder = getFuelOrderByIdQuery.process(fuelOrderId);

        fuelOrder.reject(performedBy);

        fuelOrderRepository.save(fuelOrder);
        domainEventPublisher.publishAll(fuelOrder.getDomainEvents());

        return fuelOrder;
    }
}
