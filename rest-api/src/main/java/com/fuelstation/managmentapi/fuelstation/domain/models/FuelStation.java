package com.fuelstation.managmentapi.fuelstation.domain.models;

import java.time.LocalDate;
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
    private Long id;
    private FuelStationAddress address;
    private List<FuelTank> fuelTanks;
    private List<FuelPrice> fuelPrices;
    private List<Long> assignedManagersIds;
    private FuelStationStatus status;
    private LocalDate createdAt;

    public List<FuelTank> getFuelTanksByFuelGrade(FuelGrade fuelGrade) {
        return fuelTanks.stream()
                .filter(ft -> ft.getFuelGrade() == fuelGrade)
                .toList();
    }

    public long getAvailableVolume(FuelGrade fuelGrade) {
        double totalAvailableAmount = getFuelTanksByFuelGrade(fuelGrade).stream()
                .mapToDouble(FuelTank::getAvailableVolume)
                .sum();

        return (long) totalAvailableAmount;
    }

    public void assignManager(long managerId) {
        Optional<Long> foundManagerId = assignedManagersIds.stream()
                .filter((id) -> id == managerId)
                .findFirst();

        if (foundManagerId.isPresent()) {
            throw new ManagerAlreadyAssignedException(managerId, this.id);
        }

        assignedManagersIds.add(managerId);
        pushDomainEvent(new ManagerAssignedToFuelStation(id, managerId));
    }

    public void unassignManager(long managerId) {
        assignedManagersIds.removeIf((id) -> id == managerId);
        pushDomainEvent(new ManagerUnassignedFromFuelStation(id, managerId));
    }

    public void changeFuelPrice(FuelGrade fuelGrade, float newPrice) {
        FuelPrice oldPrice = fuelPrices.stream()
                .filter(fp -> fp.fuelGrade() == fuelGrade)
                .findFirst()
                .orElseThrow(() -> new FuelGradeNotFoundException(fuelGrade.toString(), this.id));

        fuelPrices.remove(oldPrice);
        fuelPrices.add(new FuelPrice(fuelGrade, newPrice));
        pushDomainEvent(new FuelPriceChanged(id));
    }

    public void refillFuelTank(FuelTank fuelTank, float volume) {
        float totalVolume = fuelTank.getCurrentVolume() + volume;
        if (totalVolume > fuelTank.getMaxCapacity()) {
            throw new TankCapacityExceededException(
                    fuelTank.getId(), 
                    totalVolume, 
                    fuelTank.getMaxCapacity()
            );
        }

        fuelTank.setCurrentVolume(totalVolume);
        fuelTank.setLastRefillDate(Optional.of(LocalDate.now()));
    }

    public void deactivate() {
        if (status == FuelStationStatus.Deactivated) {
            throw new FuelStationAlreadyDeactivatedException(this.id);
        }

        this.status = FuelStationStatus.Deactivated;
        this.unassignAllManagers();
        pushDomainEvent(new FuelStationDeactivated(id));
    }

    private void unassignAllManagers() {
        List<Long> cloneAssignedManagersIds = new ArrayList<>(assignedManagersIds);
        for (long managerId : cloneAssignedManagersIds) {
            unassignManager(managerId);
        }
    }
}