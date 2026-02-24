package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.List;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationAccessControlChecker;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationFetcher;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;

@Component
@AllArgsConstructor
public class GetFuelStationOrders {
   
    private final FuelOrderRepository fuelOrderRepository;
    private final FuelStationFetcher fuelStationFetcher;
    private final FuelStationAccessControlChecker accessControlChecker;

    @Transactional
    public List<FuelOrder> process(long fuelStationId, Credentials credentials) {
        var fuelStation = fuelStationFetcher.fetchActiveById(fuelStationId);
        accessControlChecker.checkAccess(fuelStation, credentials);
        return fuelOrderRepository.findFuelOrdersByFuelStationId(fuelStationId);
    }
}
