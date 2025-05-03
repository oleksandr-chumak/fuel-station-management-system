package com.fuelstation.managmentapi.fuelorder.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderFactory;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderCreated;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderAmountExceedsLimitException;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;
import com.fuelstation.managmentapi.fuelstation.application.usecases.GetFuelStationById;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Component
public class CreateFuelOrder {
    
    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    @Autowired
    private GetFuelStationById getFuelStationById;

    @Autowired 
    private FuelOrderFactory fuelOrderFactory;

    @Autowired
    private DomainEventPublisher domainEventPublisher;
    
    /*
     * The ordered amount of fuel can't be greater than the available space in the fuel tanks for the specified fuel grade, 
     * minus the amount of fuel already ordered for that grade which hasn't been confirmed or rejected.
     */
    public FuelOrder process(long fuelStationId, FuelGrade fuelGrade, float amount) {
        FuelStation fuelStation = getFuelStationById.process(fuelStationId);

        float availableVolume = fuelStation.getAvailableVolume(fuelGrade);
        float pendingAmount = fuelOrderRepository.getUnconfirmedFuelAmount(fuelStationId, fuelGrade);
        float allowedAmount = availableVolume - pendingAmount;

        if(amount > allowedAmount) {
            throw new FuelOrderAmountExceedsLimitException(
                amount,
                availableVolume,
                pendingAmount,
                allowedAmount,
                fuelGrade,
                fuelStationId
            );
        }
 
        FuelOrder createdFuelOrder = fuelOrderFactory.create(fuelStationId, fuelGrade, amount);
        FuelOrder savedFuelOrder = fuelOrderRepository.save(createdFuelOrder);
        domainEventPublisher.publish(new FuelOrderCreated(savedFuelOrder.getId()));
        return savedFuelOrder;
    }

}
