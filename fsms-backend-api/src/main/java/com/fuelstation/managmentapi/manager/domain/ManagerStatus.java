package com.fuelstation.managmentapi.manager.domain;

public enum ManagerStatus {
    ACTIVE("active"),
    TERMINATED("terminated");

    private final String displayName;

    ManagerStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }

}
