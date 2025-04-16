package com.fuelstation.managmentapi.fuelstation.domain;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

public interface FuelStationService {
    FuelStation createFuelStation(String street, String buildinNumber, String city, String postalCode, String country);
    FuelStation assignManager(long fuelStationId, long managerId);
    FuelStation unassignManager(long fuelStationId, long managerId);
    FuelStation changeFuelPrice(long fuelStationId, FuelGrade fuelGrade, float newPrice);
    FuelStation processFuelDelivery(long fuelStationId, long fuelOrderId);
}