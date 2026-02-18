package com.fuelstation.managmentapi.fuelorder.domain;

import lombok.Getter;

@Getter
public enum FuelOrderStatus {
    PENDING(1, "pending"),
    CONFIRMED(2, "confirmed"),
    REJECTED(3, "rejected"),
    PROCESSED(4, "processed");

    private final long id;
    private final String name;

    FuelOrderStatus(long id, String name) {
        this.name = name;
        this.id = id;
    }

    public static FuelOrderStatus fromId(long id) {
        for (FuelOrderStatus status : FuelOrderStatus.values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid FuelOrderStatus id: " + id);
    }

    @Override
    public String toString() {
        return name;
    }
}