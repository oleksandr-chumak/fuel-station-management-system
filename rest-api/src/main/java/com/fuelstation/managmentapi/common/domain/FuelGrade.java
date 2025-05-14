package com.fuelstation.managmentapi.common.domain;

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

}
