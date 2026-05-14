package com.fuelstation.managmentapi.common.domain;

import java.util.Arrays;
import java.util.Map;

public enum CurrencyCode {
    NOK,
    UAH,
    EUR,
    USD;

    private static final Map<CountryCode, CurrencyCode> COUNTRY_CURRENCY_MAP = Map.of(
            CountryCode.DE, EUR,
            CountryCode.NO, NOK,
            CountryCode.UA, UAH
    );

    public static CurrencyCode fromCountryCode(CountryCode countryCode) {
        CurrencyCode currency = COUNTRY_CURRENCY_MAP.get(countryCode);
        if (currency == null) {
            throw new IllegalArgumentException("No currency mapped for country: " + countryCode);
        }
        return currency;
    }

    public static CurrencyCode fromString(String value) {
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