package com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;

import java.util.List;

public interface FuelGradeRepository {

    List<FuelGrade> findAll();

    List<FuelGrade> findAvailableByCountry(CountryCode countryCode);
}
