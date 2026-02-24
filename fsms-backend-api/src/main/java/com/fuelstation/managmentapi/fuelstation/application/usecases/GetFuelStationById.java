package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationAccessControlChecker;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationFetcher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Component
@AllArgsConstructor
public class GetFuelStationById {

    private final FuelStationFetcher fuelStationFetcher;
    private final FuelStationAccessControlChecker accessControlChecker;

    public FuelStation process(long fuelStationId, Credentials credentials) {
        var fuelStation = fuelStationFetcher.fetchActiveById(fuelStationId);
        accessControlChecker.checkAccess(fuelStation, credentials);
        return fuelStation;
    }

}
