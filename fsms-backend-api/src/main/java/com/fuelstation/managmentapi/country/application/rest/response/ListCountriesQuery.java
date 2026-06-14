package com.fuelstation.managmentapi.country.application.rest.response;

import com.fuelstation.managmentapi.country.application.query.CountryResponse;
import com.fuelstation.managmentapi.country.infrastructure.persistence.repository.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListCountriesQuery {

    private final CountryRepository countryRepository;

    public List<CountryResponse> handle() {
        return countryRepository.findAll().stream()
                .map(CountryResponse::fromDomain)
                .toList();
    }
}
