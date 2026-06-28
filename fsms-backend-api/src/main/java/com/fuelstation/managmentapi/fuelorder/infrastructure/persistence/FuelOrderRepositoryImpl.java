package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;

@Repository
@AllArgsConstructor
public class FuelOrderRepositoryImpl implements FuelOrderRepository {

    private JpaFuelOrderRepository jpaFuelOrderRepository;
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
    public List<FuelOrder> findPendingByFuelTankIdsAndGrade(List<Long> fuelTankIds, FuelGrade fuelGrade) {
        return jpaFuelOrderRepository.findByFuelTankIdsAndGradeAndStatus(
                fuelTankIds,
                fuelGrade.getId(),
                FuelOrderStatus.PENDING.getId()
            )
            .stream()
            .map(fuelOrderMapper::toDomain)
            .toList();
    }

    @Override
    public List<FuelOrder> findPendingByFuelTankId(long fuelTankId) {
        return jpaFuelOrderRepository.findByFuelTankIdAndStatus(
                fuelTankId,
                FuelOrderStatus.PENDING.getId()
            )
            .stream()
            .map(fuelOrderMapper::toDomain)
            .toList();
    }

    @Override
    public List<FuelOrder> findFuelOrdersByFuelStationId(long fuelStationId) {
        return jpaFuelOrderRepository.findByFuelStationId(fuelStationId).stream().map(fuelOrderMapper::toDomain).toList();
    }

    @Override
    public List<FuelOrder> findAll() {
        return jpaFuelOrderRepository.findAll(Sort.by("createdAt").descending())
            .stream()
            .map(fuelOrderMapper::toDomain)
            .toList();
    }
}
