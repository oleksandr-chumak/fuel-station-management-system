package com.fuelstation.managmentapi.fuelsale.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelsale.domain.FuelSale;
import com.fuelstation.managmentapi.fuelsale.infrastructure.persistence.mapper.FuelSaleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class FuelSaleRepositoryImpl implements FuelSaleRepository {

    private final JpaFuelSaleRepository jpaFuelSaleRepository;
    private final FuelSaleMapper fuelSaleMapper;

    @Override
    public FuelSale save(FuelSale sale) {
        var entity = fuelSaleMapper.toEntity(sale);
        entity = jpaFuelSaleRepository.save(entity);
        return fuelSaleMapper.toDomain(entity);
    }

    @Override
    public List<FuelSale> findByFuelStationId(long fuelStationId) {
        return jpaFuelSaleRepository.findByFuelStationId(fuelStationId).stream()
                .map(fuelSaleMapper::toDomain)
                .toList();
    }
}
