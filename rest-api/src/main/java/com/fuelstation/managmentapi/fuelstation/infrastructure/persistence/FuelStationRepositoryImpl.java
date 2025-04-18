package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.fuelstation.domain.FuelStationRepository;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Repository
public class FuelStationRepositoryImpl implements FuelStationRepository {

    @Autowired
    private JpaFuelStationRepository jpaFuelStationRepository;

    @Autowired 
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
    public void deleteAll() {
        jpaFuelStationRepository.deleteAll();
    }

    @Override
    public List<FuelStation> findAll() {
        return jpaFuelStationRepository.findAll().stream().map(fuelStationMapper::toDomain).toList();
    }
}