package com.fuelstation.managmentapi.fuelstation.domain.events;

import java.util.Arrays;

public enum FuelStationEventType {
    FUEL_STATION_CREATED,
    FUEL_STATION_DEACTIVATED,
    FUEL_STATION_FUEL_PRICE_CHANGED,
    MANAGER_ASSIGNED_TO_FUEL_STATION,
    MANAGER_UNASSIGNED_FROM_FUEL_STATION;

    public static FuelStationEventType fromString(String value) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown event type: " + value));
    }

    public static boolean isValid(String value) {
        return Arrays.stream(values())
                .anyMatch(e -> e.name().equalsIgnoreCase(value));
    }
}