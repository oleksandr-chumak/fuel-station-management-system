package com.fuelstation.managmentapi.fuelstation.domain.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.common.domain.AggregateRoot;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelPriceChanged;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationDeactivated;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerAssignedToFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerUnassignedFromFuelStation;
import com.fuelstation.managmentapi.manager.domain.Manager;

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

    public void processFuelDelivery(FuelOrder fuelOrder) {
    }

    public void assignManager(long managerId) {
        Optional<Long> foundManagerId = assignedManagersIds.stream().filter((id) -> id == managerId).findFirst();

        if(foundManagerId.isPresent()) {
            throw new IllegalArgumentException("Manager is already assigned to the fuel station");
        }

        assignedManagersIds.add(managerId); 
        pushDomainEvent(new ManagerAssignedToFuelStation(id, managerId));
    }

    public void unassignManager(Manager manager) {
        assignedManagersIds.removeIf((id) -> id == manager.getId());
        pushDomainEvent(new ManagerUnassignedFromFuelStation(id, manager.getId()));
    }

    public void changeFuelPrice(FuelGrade fuelGrade, float newPrice) {
        FuelPrice oldPrice = fuelPrices.stream()
            .filter(fp -> fp.fuelGrade() == fuelGrade)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Cannot find fuel price with specified fuel grade"));

        fuelPrices.remove(oldPrice);
        fuelPrices.add(new FuelPrice(fuelGrade, newPrice));
        pushDomainEvent(new FuelPriceChanged(id));
    }
    
    public void refillFuelTank(FuelTank fuelTank, float volume) {
        if(fuelTank.getCurrentVolume() + volume > fuelTank.getMaxCapacity()) {
            throw new IllegalArgumentException();
        }

        fuelTank.setCurrentVolume(fuelTank.getCurrentVolume() + volume);
        fuelTank.setLastRefillDate(Optional.of(LocalDate.now()));
    }

    public void deactivate() {
        if(status == FuelStationStatus.Deactivated) {
            throw new IllegalArgumentException();
        }
        
        this.status = FuelStationStatus.Deactivated;
        this.unassignAllManagers();
        pushDomainEvent(new FuelStationDeactivated(id));
    }

    private void unassignAllManagers() {
        List<Long> cloneAssignedManagersIds = new ArrayList<>(assignedManagersIds);
        for(long managerId : cloneAssignedManagersIds) {
            unassignManager(new Manager(managerId, null, null, null, null));
        }
    }
}