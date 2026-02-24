package com.fuelstation.managmentapi.fuelorder.application.support;

import com.fuelstation.managmentapi.fuelorder.application.exceptions.FuelOrderNotFoundException;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FuelOrderFetcher {

    private final FuelOrderRepository fuelOrderRepository;

    public FuelOrder fetchById(long fuelOrderId) {
        return fuelOrderRepository.findById(fuelOrderId)
                .orElseThrow(() -> new FuelOrderNotFoundException(fuelOrderId));
    }

}
