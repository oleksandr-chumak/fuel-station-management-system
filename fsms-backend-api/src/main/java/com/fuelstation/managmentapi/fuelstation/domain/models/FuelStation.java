package com.fuelstation.managmentapi.fuelstation.domain.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.AggregateRoot;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelPriceChanged;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationDeactivated;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelTankDecommissioned;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelTankVolumeChanged;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerAssignedToFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerUnassignedFromFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelGradeNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationAlreadyDeactivatedException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelTankAlreadyDecommissionedException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelTankNotEmptyException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelTankNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.InsufficientFuelVolumeException;
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
    private List<FuelStationFuelPrice> fuelPrices;
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
        FuelStationFuelPrice oldPrice = fuelPrices.stream()
                .filter(fp -> fp.fuelGrade() == fuelGrade)
                .findFirst()
                .orElseThrow(() -> new FuelGradeNotFoundException(fuelGrade.toString(), fuelStationId));

        fuelPrices.remove(oldPrice);
        fuelPrices.add(new FuelStationFuelPrice(fuelGrade, newPrice, oldPrice.currency()));
        pushDomainEvent(new FuelPriceChanged(fuelStationId, fuelGrade, newPrice, oldPrice.currency(), performedBy));
    }

    public void installFuelTank(FuelGrade fuelGrade, BigDecimal maxCapacity, Actor performedBy) {
        if (maxCapacity == null || maxCapacity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("maxCapacity must be greater than zero");
        }
        this.fuelTanks.add(new FuelTank(
            null,
            fuelGrade,
            BigDecimal.ZERO,
            maxCapacity,
            Optional.empty(),
            FuelTankStatus.ACTIVE
        ));
    }

    public void decommissionFuelTank(long fuelTankId, Actor performedBy) {
        FuelTank fuelTank = findFuelTankById(fuelTankId);

        if (fuelTank.isDecommissioned()) {
            throw new FuelTankAlreadyDecommissionedException(this.fuelStationId, fuelTankId);
        }

        if (fuelTank.getCurrentVolume().compareTo(BigDecimal.ZERO) > 0) {
            throw new FuelTankNotEmptyException(this.fuelStationId, fuelTankId, fuelTank.getCurrentVolume());
        }

        fuelTank.setStatus(FuelTankStatus.DECOMMISSIONED);
        pushDomainEvent(new FuelTankDecommissioned(this.fuelStationId, fuelTankId, performedBy));
    }

    public void refillFuelTank(long fuelTankId, BigDecimal volume, Actor performedBy) {
        refillFuelTank(findFuelTankById(fuelTankId), volume, performedBy);
    }

    public void refillFuelTank(FuelTank fuelTank, BigDecimal volume, Actor performedBy) {
        var oldVolume = fuelTank.getCurrentVolume();
        var newVolume = oldVolume.add(volume);
        if (newVolume.compareTo(fuelTank.getMaxCapacity()) > 0) {
            throw new TankCapacityExceededException(
                    fuelTank.getId(),
                    newVolume,
                    fuelTank.getMaxCapacity()
            );
        }

        fuelTank.setCurrentVolume(newVolume);
        fuelTank.setLastRefillDate(Optional.of(OffsetDateTime.now()));
        pushDomainEvent(new FuelTankVolumeChanged(
                this.fuelStationId,
                fuelTank.getId(),
                fuelTank.getFuelGrade(),
                oldVolume,
                newVolume,
                FuelTankVolumeChangeReason.REFILL,
                performedBy
        ));
    }

    public void dispenseFuel(long fuelTankId, BigDecimal volume, Actor performedBy) {
        FuelTank fuelTank = findFuelTankById(fuelTankId);
        var oldVolume = fuelTank.getCurrentVolume();
        if (oldVolume.compareTo(volume) < 0) {
            throw new InsufficientFuelVolumeException(
                    fuelTank.getId(),
                    volume,
                    oldVolume
            );
        }

        var newVolume = oldVolume.subtract(volume);
        fuelTank.setCurrentVolume(newVolume);
        pushDomainEvent(new FuelTankVolumeChanged(
                this.fuelStationId,
                fuelTank.getId(),
                fuelTank.getFuelGrade(),
                oldVolume,
                newVolume,
                FuelTankVolumeChangeReason.DISPENSE,
                performedBy
        ));
    }

    private FuelTank findFuelTankById(long fuelTankId) {
        return this.fuelTanks.stream()
                .filter(t -> Objects.equals(t.getId(), fuelTankId))
                .findFirst()
                .orElseThrow(() -> new FuelTankNotFoundException(this.fuelStationId, fuelTankId));
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

    public static FuelStation create(
        FuelStationAddress address,
        List<FuelTank> fuelTanks,
        List<FuelStationFuelPrice> fuelPrices
    ) {
        return new FuelStation(
            null,
            address,
            fuelTanks,
            fuelPrices,
            new ArrayList<>(),
            FuelStationStatus.ACTIVE,
            OffsetDateTime.now()
        );
    }
}