package com.fuelstation.managmentapi.fuelstation.domain.models;

import lombok.Getter;

@Getter
public enum FuelStationStatus {
    ACTIVE(1, "active"),
    DEACTIVATED(2, "deactivated");

    private final long id;
    private final String name;
    
    FuelStationStatus(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static FuelStationStatus fromId(long id) {
        for (FuelStationStatus status : FuelStationStatus.values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid FuelStationStatus id: " + id);
    }

    @Override
    public String toString() {
        return this.name;
    }
}