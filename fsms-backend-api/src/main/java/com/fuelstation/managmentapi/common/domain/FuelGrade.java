package com.fuelstation.managmentapi.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum FuelGrade {
    RON_92(1, "ron-92"),
    RON_95(2, "ron-95"),
    DIESEL(3, "diesel");

    private final long id;
    private final String name;

    FuelGrade(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static FuelGrade fromId(long id) {
        for (FuelGrade grade : FuelGrade.values()) {
            if (grade.id == id) {
                return grade;
            }
        }
        throw new IllegalArgumentException("Invalid FuelGrade id: " + id);
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
