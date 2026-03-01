package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.impl;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationMapper;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationEntity;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.jpa.JpaFuelStationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Repository
@AllArgsConstructor
public class FuelStationRepositoryImpl implements FuelStationRepository {

    private JpaFuelStationRepository jpaFuelStationRepository;
    private FuelStationMapper fuelStationMapper;

    @Override
    public FuelStation save(FuelStation fuelStation) {
        FuelStationEntity fuelStationEntity = jpaFuelStationRepository.save(fuelStationMapper.toEntity(fuelStation));
        return fuelStationMapper.toDomain(fuelStationEntity);
    }

    @Override
    public Optional<FuelStation> findById(Long id) {
        Optional<FuelStationEntity> fuelStationEntity = jpaFuelStationRepository.findById(id);
        return fuelStationEntity.map(fuelStationMapper::toDomain);
    }

    @Override
    public List<FuelStation> findAll() {
        return jpaFuelStationRepository.findAll().stream().map(fuelStationMapper::toDomain).toList();
    }

    @Override
    public List<FuelStation> findFuelStationsByManagerId(long managerId) {
        return jpaFuelStationRepository.findByManagerId(managerId).stream().map(fuelStationMapper::toDomain).toList();
    }
}