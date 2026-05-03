package com.fuelstation.managmentapi.common.domain;

public enum FuelUnit {
    LITER,
    GALLON;

    public static FuelUnit fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("FuelUnit value cannot be null");
        }
        return FuelUnit.valueOf(value.trim().toUpperCase());
    }
}