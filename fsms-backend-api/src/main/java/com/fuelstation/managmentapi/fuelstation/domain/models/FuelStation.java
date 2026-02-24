package com.fuelstation.managmentapi.fuelstation.domain.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void assignManager(long credentialsId) {
        if (isManagerAssigned(credentialsId)) {
            throw new ManagerAlreadyAssignedException(credentialsId, fuelStationId);
        }

        assignedManagersIds.add(credentialsId);
        pushDomainEvent(new ManagerAssignedToFuelStation(fuelStationId, credentialsId));
    }

    public void unassignManager(long credentialsId) {
        assignedManagersIds.removeIf((id) -> id == credentialsId);
        pushDomainEvent(new ManagerUnassignedFromFuelStation(fuelStationId, credentialsId));
    }

    public boolean isManagerAssigned(long credentialsId) {
        return assignedManagersIds.contains(credentialsId);
    }

    public void changeFuelPrice(FuelGrade fuelGrade, BigDecimal newPrice) {
        FuelPrice oldPrice = fuelPrices.stream()
                .filter(fp -> fp.fuelGrade() == fuelGrade)
                .findFirst()
                .orElseThrow(() -> new FuelGradeNotFoundException(fuelGrade.toString(), fuelStationId));

        fuelPrices.remove(oldPrice);
        fuelPrices.add(new FuelPrice(fuelGrade, newPrice));
        pushDomainEvent(new FuelPriceChanged(fuelStationId, fuelGrade, newPrice));
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

    public void deactivate() {
        if (status == FuelStationStatus.DEACTIVATED) {
            throw new FuelStationAlreadyDeactivatedException(this.fuelStationId);
        }

        this.status = FuelStationStatus.DEACTIVATED;
        this.unassignAllManagers();
        pushDomainEvent(new FuelStationDeactivated(fuelStationId));
    }

    private void unassignAllManagers() {
        List<Long> cloneAssignedManagersIds = new ArrayList<>(assignedManagersIds);
        for (long credentialsId : cloneAssignedManagersIds) {
            unassignManager(credentialsId);
        }
    }
}