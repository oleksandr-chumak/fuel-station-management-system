package com.fuelstation.managmentapi.authentication.domain;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE(1,"active"),
    TERMINATED(2,"terminated");

    private final long id;
    private final String name;

    UserStatus(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static UserStatus fromId(long id) {
        for (UserStatus status : UserStatus.values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ManagerStatus id: " + id);
    }

    @Override
    public String toString() {
        return this.name;
    }

}
