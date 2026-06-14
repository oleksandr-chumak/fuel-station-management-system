package com.fuelstation.managmentapi.country.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.country.domain.Country;
import com.fuelstation.managmentapi.country.infrastructure.persistence.mapper.CountryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CountryRepositoryImpl implements CountryRepository {

    private final JpaCountryRepository jpaCountryRepository;
    private final CountryMapper countryMapper;

    @Override
    public List<Country> findAll() {
        return jpaCountryRepository.findAll().stream()
                .map(countryMapper::toDomain)
                .toList();
    }
}
