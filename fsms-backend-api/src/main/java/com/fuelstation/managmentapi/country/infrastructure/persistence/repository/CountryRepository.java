package com.fuelstation.managmentapi.country.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.country.domain.Country;

import java.util.List;

public interface CountryRepository {

    List<Country> findAll();
}
