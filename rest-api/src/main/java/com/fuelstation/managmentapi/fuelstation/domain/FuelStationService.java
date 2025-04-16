package com.fuelstation.managmentapi.fuelstation.domain;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

public interface FuelStationService {
    void createFuelStation(String street, String buildinNumber, String city, String postalCode, String country);
    void assignManager(int gasStationId, int managerId);
    void unassignManager(int gasStationId, int managerId);
    void changeFuelPrice(int gasStationId, FuelGrade fuelGrade, float newPrice);
    void processFuelDelivery(int fuelOrderId);
}