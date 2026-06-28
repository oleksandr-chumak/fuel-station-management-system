package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FuelTankHasPendingFuelOrdersException extends FuelStationDomainException {

    public FuelTankHasPendingFuelOrdersException(long fuelStationId, long fuelTankId, List<Long> pendingFuelOrderIds) {
        super(
            String.format(
                "Fuel tank %d in fuel station %d cannot be decommissioned while %d pending fuel order(s) reference it: %s",
                fuelTankId, fuelStationId, pendingFuelOrderIds.size(), pendingFuelOrderIds
            ),
            "TANK_HAS_PENDING_FUEL_ORDERS",
            buildDetails(fuelStationId, fuelTankId, pendingFuelOrderIds)
        );
    }

    private static Map<String, Object> buildDetails(long fuelStationId, long fuelTankId, List<Long> pendingFuelOrderIds) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("fuelStationId", fuelStationId);
        details.put("fuelTankId", fuelTankId);
        details.put("pendingFuelOrderIds", List.copyOf(pendingFuelOrderIds));
        return details;
    }
}
