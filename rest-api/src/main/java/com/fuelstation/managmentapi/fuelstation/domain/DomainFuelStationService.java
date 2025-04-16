package com.fuelstation.managmentapi.fuelstation.domain;

import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

@Service
public class DomainFuelStationService implements FuelStationService {
    
    @Override
    public void createFuelStation(String street, String buildinNumber, String city, String postalCode, String country) {
        // Implementation
    }

    @Override
    public void assignManager(int gasStationId, int managerId) {
        // Implementation
    }

    @Override
    public void unassignManager(int gasStationId, int managerId) {
        // Implementation
    }

    @Override
    public void changeFuelPrice(int gasStationId, FuelGrade fuelGrade, float newPrice) {
        // Implementation
    }

    @Override
    public void processFuelDelivery(int fuelOrderId) {
        // Implementation
    }
}