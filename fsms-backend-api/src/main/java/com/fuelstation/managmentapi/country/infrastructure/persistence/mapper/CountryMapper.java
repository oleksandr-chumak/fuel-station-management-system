package com.fuelstation.managmentapi.country.infrastructure.persistence.mapper;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.country.domain.Country;
import com.fuelstation.managmentapi.country.infrastructure.persistence.model.CountryEntity;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {

    public Country toDomain(CountryEntity entity) {
        return new Country(
                entity.getCountryId(),
                CountryCode.valueOf(entity.getCountryCode())
        );
    }
}
