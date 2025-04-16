package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.fuelstation.domain.FuelStationRepository;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Repository
public class FuelStationRepositoryImpl implements FuelStationRepository {

    @Override
    public FuelStation save(FuelStation fuelStation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Optional<FuelStation> findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }
    
}
