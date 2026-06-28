package com.fuelstation.managmentapi.fuelorder.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelorder.application.usecases.command.CreateFuelOrderCommand;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderAllocation;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderAllocationExceedsLimitException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderDuplicateAllocationException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderTankGradeMismatchException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderTankNotOnStationException;
import com.fuelstation.managmentapi.fuelstation.application.exceptions.FuelStationAccessDeniedException;
import com.fuelstation.managmentapi.fuelstation.application.query.GetActiveFuelStationByIdQuery;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTank;
import lombok.AllArgsConstructor;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderCreated;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CreateFuelOrder {

    private final FuelOrderRepository fuelOrderRepository;
    private final GetActiveFuelStationByIdQuery getActiveFuelStationByIdQuery;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public FuelOrder process(CreateFuelOrderCommand cmd) {
        var fuelStation = getActiveFuelStationByIdQuery.process(cmd.fuelStationId(), cmd.performedBy());
        checkAccess(fuelStation, cmd.performedBy());

        var allocationTankIds = cmd.allocations().stream()
            .map(FuelOrderAllocation::fuelTankId)
            .toList();
        if (allocationTankIds.size() != Set.copyOf(allocationTankIds).size()) {
            throw new FuelOrderDuplicateAllocationException();
        }

        var stationTanksById = fuelStation.getFuelTanks().stream()
            .collect(Collectors.toMap(FuelTank::getId, t -> t));
        for (var allocation : cmd.allocations()) {
            var fuelTank = stationTanksById.get(allocation.fuelTankId());
            if (fuelTank == null) {
                throw new FuelOrderTankNotOnStationException(allocation.fuelTankId(), cmd.fuelStationId());
            }
            if (fuelTank.getFuelGrade() != cmd.fuelGrade()) {
                throw new FuelOrderTankGradeMismatchException(
                    fuelTank.getId(),
                    fuelTank.getFuelGrade(),
                    cmd.fuelGrade(),
                    cmd.fuelStationId()
                );
            }
        }

        Map<Long, BigDecimal> pendingVolumes = new HashMap<>();
        var pendingOrders = fuelOrderRepository.findPendingByFuelTankIdsAndGrade(allocationTankIds, cmd.fuelGrade());
        for (var order : pendingOrders) {
            for (var allocation : order.getAllocations()) {
                var fuelTankId = allocation.fuelTankId();
                if (allocationTankIds.contains(fuelTankId)) {
                    var accumulatedVolume = pendingVolumes.getOrDefault(fuelTankId, BigDecimal.ZERO);
                    pendingVolumes.put(fuelTankId, accumulatedVolume.add(allocation.volume()));
                }
            }
        }

        for (var allocation : cmd.allocations()) {
            var fuelTankId = allocation.fuelTankId();
            var fuelTank = stationTanksById.get(fuelTankId);

            var allocationVolume = allocation.volume();
            var pendingVolume = pendingVolumes.getOrDefault(fuelTankId, BigDecimal.ZERO);
            var availableVolume = fuelTank.getAvailableVolume();
            var allowedVolume = availableVolume.subtract(pendingVolume);
            var volumeAfterAllocation = allowedVolume.subtract(allocationVolume);

            if (volumeAfterAllocation.signum() < 0) {
                throw new FuelOrderAllocationExceedsLimitException(
                    fuelTankId,
                    allocationVolume,
                    availableVolume,
                    pendingVolume,
                    allowedVolume,
                    cmd.fuelGrade(),
                    cmd.fuelStationId()
                );
            }
        }

        var createdFuelOrder = FuelOrder.create(
            cmd.fuelStationId(),
            cmd.allocations(),
            cmd.fuelGrade(),
            cmd.performedBy()
        );
        var savedFuelOrder = fuelOrderRepository.save(createdFuelOrder);
        domainEventPublisher.publish(new FuelOrderCreated(
            savedFuelOrder.getFuelOrderId(),
            cmd.fuelStationId(),
            cmd.performedBy()
        ));

        return savedFuelOrder;
    }

    private void checkAccess(FuelStation fuelStation, Actor actor) {
        if(actor.isSystem() || actor.isAdmin()) {
            return;
        }
        if(actor.isManager() && !fuelStation.isManagerAssigned(actor.id())) {
            throw new FuelStationAccessDeniedException(fuelStation.getFuelStationId(), actor);
        }
    }

}
