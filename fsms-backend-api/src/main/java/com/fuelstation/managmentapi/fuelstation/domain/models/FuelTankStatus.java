package com.fuelstation.managmentapi.fuelstation.domain.models;

import lombok.Getter;

@Getter
public enum FuelTankStatus {
    ACTIVE(1, "active"),
    DECOMMISSIONED(2, "decommissioned");

    private final long id;
    private final String name;

    FuelTankStatus(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static FuelTankStatus fromId(long id) {
        for (FuelTankStatus status : FuelTankStatus.values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid FuelTankStatus id: " + id);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
