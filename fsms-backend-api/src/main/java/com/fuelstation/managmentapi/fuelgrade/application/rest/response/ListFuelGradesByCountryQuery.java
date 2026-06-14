package com.fuelstation.managmentapi.fuelgrade.application.rest.response;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.fuelgrade.application.query.FuelGradeResponse;
import com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.repository.FuelGradeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListFuelGradesByCountryQuery {

    private final FuelGradeRepository fuelGradeRepository;

    public List<FuelGradeResponse> handle(CountryCode countryCode) {
        return fuelGradeRepository.findAvailableByCountry(countryCode).stream()
                .map(FuelGradeResponse::fromDomain)
                .toList();
    }
}
