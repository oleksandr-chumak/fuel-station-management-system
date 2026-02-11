package com.fuelstation.managmentapi.manager.domain;

import lombok.Getter;

@Getter
public enum ManagerStatus {
    ACTIVE(1,"active"),
    TERMINATED(2,"terminated");

    private final long id;
    private final String name;

    ManagerStatus(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ManagerStatus fromId(long id) {
        for (ManagerStatus status : ManagerStatus.values()) {
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
