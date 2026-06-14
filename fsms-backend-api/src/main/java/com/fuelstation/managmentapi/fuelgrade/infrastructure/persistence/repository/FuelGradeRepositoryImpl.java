package com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.mapper.FuelGradeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class FuelGradeRepositoryImpl implements FuelGradeRepository {

    private final JpaFuelGradeRepository jpaFuelGradeRepository;
    private final FuelGradeMapper fuelGradeMapper;

    @Override
    public List<FuelGrade> findAll() {
        return jpaFuelGradeRepository.findAll().stream()
                .map(fuelGradeMapper::toDomain)
                .toList();
    }

    @Override
    public List<FuelGrade> findAvailableByCountry(CountryCode countryCode) {
        return jpaFuelGradeRepository.findAvailableByCountryId(countryCode.getId()).stream()
                .map(fuelGradeMapper::toDomain)
                .toList();
    }
}
