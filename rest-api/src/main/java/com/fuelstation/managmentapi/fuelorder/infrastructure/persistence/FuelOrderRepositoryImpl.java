package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderRepository;

@Repository
public class FuelOrderRepositoryImpl implements FuelOrderRepository {

    private final JpaFuelOrderRepository jpaFuelOrderRepository;

    public FuelOrderRepositoryImpl(JpaFuelOrderRepository jpaFuelOrderRepository) {
        this.jpaFuelOrderRepository = jpaFuelOrderRepository;
    }

    @Override
    public FuelOrder save(FuelOrder fuelOrder) {
        FuelOrderEntity entity = FuelOrderMapper.toEntity(fuelOrder);
        entity = jpaFuelOrderRepository.save(entity);
        return FuelOrderMapper.toDomain(entity);
    }

    @Override
    public Optional<FuelOrder> findById(long id) {
        return jpaFuelOrderRepository.findById(id)
                .map(FuelOrderMapper::toDomain);
    }

    @Override
    public float getUnconfirmedFuelAmount(Long gasStationId, FuelGrade fuelGrade) {
        Float res = jpaFuelOrderRepository.getUnconfirmedAmountByGradeAndStation(gasStationId, fuelGrade);
        return res != null ? res : 0f;
    }

    @Override
    public void deleteAll() {
        jpaFuelOrderRepository.deleteAll();
    }
}
