package com.fuelstation.managmentapi.fuelstation.domain.models;

public enum FuelStationStatus {
    ACTIVE("active"),
    DEACTIVATED("deactivated");

    private final String displayName;
    
    FuelStationStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}