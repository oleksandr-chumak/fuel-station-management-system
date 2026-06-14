package com.fuelstation.managmentapi.country.application.query;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.country.domain.Country;

public record CountryResponse(
        Long countryId,
        CountryCode code
) {
    public static CountryResponse fromDomain(Country country) {
        return new CountryResponse(country.countryId(), country.code());
    }
}
