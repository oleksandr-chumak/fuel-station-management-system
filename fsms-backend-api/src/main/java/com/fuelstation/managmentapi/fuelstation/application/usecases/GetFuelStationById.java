package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.application.UserFetcher;
import com.fuelstation.managmentapi.common.domain.Actor;
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
    private final UserFetcher userFetcher;

    public FuelStation process(long fuelStationId, Actor performedBy) {
        var fuelStation = fuelStationFetcher.fetchActiveById(fuelStationId);
        var credentials = userFetcher.fetchById(performedBy.id());
        accessControlChecker.checkAccess(fuelStation, credentials);
        return fuelStation;
    }

}
