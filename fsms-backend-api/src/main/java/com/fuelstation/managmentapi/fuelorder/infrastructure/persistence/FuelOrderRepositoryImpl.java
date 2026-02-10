package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;

@Repository
public class FuelOrderRepositoryImpl implements FuelOrderRepository {

    @Autowired
    private JpaFuelOrderRepository jpaFuelOrderRepository;

    @Autowired
    private FuelOrderMapper fuelOrderMapper;

    @Override
    public FuelOrder save(FuelOrder fuelOrder) {
        FuelOrderEntity entity = fuelOrderMapper.toEntity(fuelOrder);
        entity = jpaFuelOrderRepository.save(entity);
        return fuelOrderMapper.toDomain(entity);
    }

    @Override
    public Optional<FuelOrder> findById(long id) {
        return jpaFuelOrderRepository.findById(id)
                .map(fuelOrderMapper::toDomain);
    }

    @Override
    public BigDecimal getUnconfirmedFuelAmount(long fuelStationId, FuelGrade fuelGrade) {
        BigDecimal res = jpaFuelOrderRepository.getUnconfirmedAmountByGradeAndStation(fuelStationId, fuelGrade);
        return res != null ? res : BigDecimal.ZERO;
    }

    @Override
    public List<FuelOrder> findFuelOrdersByFuelStationId(long fuelStationId) {
        return jpaFuelOrderRepository.findByFuelStationId(fuelStationId).stream().map(fuelOrderMapper::toDomain).toList();
    }

    @Override
    public List<FuelOrder> findAll() {
        return jpaFuelOrderRepository.findAll().stream().map(fuelOrderMapper::toDomain).toList();
    }
}
