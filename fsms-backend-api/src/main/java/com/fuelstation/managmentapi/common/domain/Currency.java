package com.fuelstation.managmentapi.common.domain;

import java.util.Arrays;

public enum Currency {
    NOK,
    UAH,
    EUR,
    USD;

    public static Currency fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Currency value cannot be null");
        }
        String normalized = value.trim().toUpperCase();
        return Arrays.stream(values())
            .filter(currency -> currency.name().equals(normalized))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Unknown Currency: '" + value + "'. Valid values: "
                    + Arrays.toString(values())
            ));
    }
}