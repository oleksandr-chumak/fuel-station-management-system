package com.fuelstation.managmentapi.fuelstation.domain.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.AggregateRoot;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelPriceChanged;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationDeactivated;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerAssignedToFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerUnassignedFromFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelGradeNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationAlreadyDeactivatedException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.ManagerAlreadyAssignedException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.TankCapacityExceededException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FuelStation extends AggregateRoot {
    private Long fuelStationId;
    private FuelStationAddress address;
    private List<FuelTank> fuelTanks;
    private List<FuelPrice> fuelPrices;
    private List<Long> assignedManagersIds;
    private FuelStationStatus status;
    private OffsetDateTime createdAt;

    public boolean deactivated() {
        return status == FuelStationStatus.DEACTIVATED;
    }

    public List<FuelTank> getFuelTanksByFuelGrade(FuelGrade fuelGrade) {
        return fuelTanks.stream()
                .filter(ft -> ft.getFuelGrade() == fuelGrade)
                .toList();
    }

    public BigDecimal getAvailableVolume(FuelGrade fuelGrade) {
        return getFuelTanksByFuelGrade(fuelGrade).stream()
                .map(FuelTank::getAvailableVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void assignManager(long managerId, Actor performedBy) {
        if (isManagerAssigned(managerId)) {
            throw new ManagerAlreadyAssignedException(managerId, fuelStationId);
        }

        assignedManagersIds.add(managerId);
        pushDomainEvent(new ManagerAssignedToFuelStation(fuelStationId, managerId, performedBy));
    }

    public void unassignManager(long managerId, Actor performedBy) {
        assignedManagersIds.removeIf((id) -> id == managerId);
        pushDomainEvent(new ManagerUnassignedFromFuelStation(fuelStationId, managerId, performedBy));
    }

    public boolean isManagerAssigned(long credentialsId) {
        return assignedManagersIds.contains(credentialsId);
    }

    public void changeFuelPrice(FuelGrade fuelGrade, BigDecimal newPrice, Actor performedBy) {
        FuelPrice oldPrice = fuelPrices.stream()
                .filter(fp -> fp.fuelGrade() == fuelGrade)
                .findFirst()
                .orElseThrow(() -> new FuelGradeNotFoundException(fuelGrade.toString(), fuelStationId));

        fuelPrices.remove(oldPrice);
        fuelPrices.add(new FuelPrice(fuelGrade, newPrice));
        pushDomainEvent(new FuelPriceChanged(fuelStationId, fuelGrade, newPrice, performedBy));
    }

    public void refillFuelTank(FuelTank fuelTank, BigDecimal volume) {
        var totalVolume = fuelTank.getCurrentVolume().add(volume);
        if (totalVolume.compareTo(fuelTank.getMaxCapacity()) > 0) {
            throw new TankCapacityExceededException(
                    fuelTank.getId(),
                    totalVolume,
                    fuelTank.getMaxCapacity()
            );
        }

        fuelTank.setCurrentVolume(totalVolume);
        fuelTank.setLastRefillDate(Optional.of(OffsetDateTime.now()));
    }

    public void deactivate(Actor performedBy) {
        if (status == FuelStationStatus.DEACTIVATED) {
            throw new FuelStationAlreadyDeactivatedException(this.fuelStationId);
        }

        this.status = FuelStationStatus.DEACTIVATED;
        this.unassignAllManagers(performedBy);
        pushDomainEvent(new FuelStationDeactivated(fuelStationId, performedBy));
    }

    private void unassignAllManagers(Actor performedBy) {
        List<Long> cloneAssignedManagersIds = new ArrayList<>(assignedManagersIds);
        for (long credentialsId : cloneAssignedManagersIds) {
            unassignManager(credentialsId, performedBy);
        }
    }
}