package com.fuelstation.managmentapi.authentication.domain;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMINISTRATOR(1, "administrator"),
    MANAGER(2, "manager");

    private final long id;
    private final String name;

    UserRole(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static UserRole fromId(long id) {
        for (UserRole role : UserRole.values()) {
            if (role.id == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid UserRole id: " + id);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
