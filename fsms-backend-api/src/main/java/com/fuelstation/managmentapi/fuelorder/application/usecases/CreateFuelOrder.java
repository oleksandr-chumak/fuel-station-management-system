package com.fuelstation.managmentapi.fuelorder.application.usecases;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderFactory;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderCreated;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderAmountExceedsLimitException;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;
import com.fuelstation.managmentapi.fuelstation.application.usecases.GetFuelStationById;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CreateFuelOrder {
    
    private final FuelOrderRepository fuelOrderRepository;
    private final GetFuelStationById getFuelStationById;
    private final FuelOrderFactory fuelOrderFactory;
    private final DomainEventPublisher domainEventPublisher;

    /*
     * The ordered amount of fuel can't be greater than the available space in the fuel tanks for the specified fuel grade, 
     * minus the amount of fuel already ordered for that grade which hasn't been confirmed or rejected.
     */
    @Transactional
    public FuelOrder process(long fuelStationId, FuelGrade fuelGrade, BigDecimal amount, Credentials credentials) {
        var fuelStation = getFuelStationById.process(fuelStationId, credentials);

        var availableVolume = fuelStation.getAvailableVolume(fuelGrade);
        var pendingAmount = fuelOrderRepository.getUnconfirmedFuelAmount(fuelStationId, fuelGrade);
        var allowedAmount = availableVolume.subtract(pendingAmount);

        if(amount.compareTo(allowedAmount) > 0) {
            throw new FuelOrderAmountExceedsLimitException(
                amount,
                availableVolume,
                pendingAmount,
                allowedAmount,
                fuelGrade,
                fuelStationId
            );
        }
 
        var createdFuelOrder = fuelOrderFactory.create(fuelStationId, fuelGrade, amount);
        var savedFuelOrder = fuelOrderRepository.save(createdFuelOrder);
        domainEventPublisher.publish(new FuelOrderCreated(savedFuelOrder.getFuelOrderId()));
        return savedFuelOrder;
    }

}
