package com.fuelstation.managmentapi.fuelpurchase.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelpurchase.domain.FuelPurchase;
import com.fuelstation.managmentapi.fuelpurchase.infrastructure.persistence.mapper.FuelPurchaseMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class FuelPurchaseRepositoryImpl implements FuelPurchaseRepository {

    private final JpaFuelPurchaseRepository jpaFuelPurchaseRepository;
    private final FuelPurchaseMapper fuelPurchaseMapper;

    @Override
    public FuelPurchase save(FuelPurchase purchase) {
        var entity = fuelPurchaseMapper.toEntity(purchase);
        entity = jpaFuelPurchaseRepository.save(entity);
        return fuelPurchaseMapper.toDomain(entity);
    }

    @Override
    public List<FuelPurchase> findByFuelStationId(long fuelStationId) {
        return jpaFuelPurchaseRepository.findByFuelStationId(fuelStationId).stream()
                .map(fuelPurchaseMapper::toDomain)
                .toList();
    }
}
