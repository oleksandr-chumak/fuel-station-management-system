package com.fuelstation.managmentapi.fuelorder.domain;

// TODO use Processed
public enum FuelOrderStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    REJECTED("rejected"),
    PROCESSED("processed");

    private final String displayName;

    FuelOrderStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}