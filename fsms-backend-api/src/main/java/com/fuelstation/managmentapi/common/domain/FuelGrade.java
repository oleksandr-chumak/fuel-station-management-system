package com.fuelstation.managmentapi.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FuelGrade {
    RON_92("ron-92"),
    RON_95("ron-95"),
    DIESEL("diesel");

    private final String displayName;

    FuelGrade(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static FuelGrade fromString(String value) {
        if (value == null) {
            return null;
        }
        
        for (FuelGrade grade : FuelGrade.values()) {
            if (grade.toString().equalsIgnoreCase(value) ||
                    grade.name().equalsIgnoreCase(value)) {
                return grade;
            }
        }
        
        throw new IllegalArgumentException("Unknown fuel grade: " + value);
    }
}
