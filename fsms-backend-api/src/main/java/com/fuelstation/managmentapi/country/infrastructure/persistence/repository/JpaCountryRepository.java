package com.fuelstation.managmentapi.country.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.country.infrastructure.persistence.model.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCountryRepository extends JpaRepository<CountryEntity, Long> {
}
