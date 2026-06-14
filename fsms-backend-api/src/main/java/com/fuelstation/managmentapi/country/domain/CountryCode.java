package com.fuelstation.managmentapi.country.domain;

import lombok.Getter;

@Getter
public enum CountryCode {
    DE(1),
    NO(2),
    UA(3);

    private final long id;

    CountryCode(long id) {
        this.id = id;
    }

    public static CountryCode fromId(long id) {
        for (CountryCode code : CountryCode.values()) {
            if (code.id == id) {
                return code;
            }
        }
        throw new IllegalArgumentException("Invalid CountryCode id: " + id);
    }
}
