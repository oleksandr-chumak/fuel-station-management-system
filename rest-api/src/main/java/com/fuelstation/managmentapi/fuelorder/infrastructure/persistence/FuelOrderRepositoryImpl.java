package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderRepository;

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
    public float getUnconfirmedFuelAmount(Long gasStationId, FuelGrade fuelGrade) {
        Float res = jpaFuelOrderRepository.getUnconfirmedAmountByGradeAndStation(gasStationId, fuelGrade);
        return res != null ? res : 0f;
    }

    @Override
    public void deleteAll() {
        jpaFuelOrderRepository.deleteAll();
    }

    @Override
    public List<FuelOrder> findFuelOrdersByFuelStationId(Long fuelStationId) {
        return jpaFuelOrderRepository.findByFuelStationId(fuelStationId).stream().map(fuelOrderMapper::toDomain).toList();
    }

    @Override
    public List<FuelOrder> findAll() {
        return jpaFuelOrderRepository.findAll().stream().map(fuelOrderMapper::toDomain).toList();
    }
}
