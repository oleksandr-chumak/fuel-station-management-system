package com.fuelstation.managmentapi.authentication.domain;

public enum UserRole {
    MANAGER("manager"),
    ADMINISTRATOR("administrator");    

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
