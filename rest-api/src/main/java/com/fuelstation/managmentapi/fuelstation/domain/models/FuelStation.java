package com.fuelstation.managmentapi.fuelstation.domain.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.common.domain.AggregateRoot;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelDeliveryWasProcessed;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelPriceWasChanged;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerWasAssignedToFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerWasUnassignedFromFuelStation;
import com.fuelstation.managmentapi.manager.domain.Manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
        if (fuelOrder.getStatus() != FuelOrderStatus.Confirmed) {
            throw new IllegalStateException("Fuel order must be in Confirmed status to process.");
        }
    
    
        if (getAvailableVolume(fuelOrder.getGrade()) < fuelOrder.getAmount()) {
            throw new IllegalStateException("Not enough tank capacity to process fuel delivery.");
        }
    
        float remainingFuel = fuelOrder.getAmount();
        for (FuelTank fuelTank : getFuelTanksByFuelGrade(fuelOrder.getGrade())) {
            boolean tankHasEnoughSpaceForAllRemainingFuel = (fuelTank.getAvailableVolume() - remainingFuel) >= 0;
    
            if (tankHasEnoughSpaceForAllRemainingFuel) {
                fuelTank.setCurrentVolume(fuelTank.getCurrentVolume() + remainingFuel);
                fuelTank.setLastRefillDate(Optional.of(LocalDate.now()));
                return;
            } else {
                remainingFuel -= fuelTank.getAvailableVolume();
                fuelTank.setCurrentVolume(fuelTank.getMaxCapacity());
                fuelTank.setLastRefillDate(Optional.of(LocalDate.now()));
            }
        }
        pushDomainEvent(new FuelDeliveryWasProcessed(fuelOrder.getId()));
    }

    public void assignManager(Manager manager) {
        Optional<Long> managerId = assignedManagersIds.stream().filter((id) -> id == manager.getId()).findFirst();

        if(managerId.isPresent()) {
            throw new IllegalArgumentException("Manager is already assigned to the fuel station");
        }

        assignedManagersIds.add(manager.getId()); 
        pushDomainEvent(new ManagerWasAssignedToFuelStation(id, manager.getId()));
    }

    public void unassignManager(Manager manager) {
        assignedManagersIds.removeIf((id) -> id == manager.getId());
        pushDomainEvent(new ManagerWasUnassignedFromFuelStation(id, manager.getId()));
    }
    
    public void unassignAllManagers() {
        List<Long> cloneAssignedManagersIds = new ArrayList<>(assignedManagersIds);
        for(long managerId : cloneAssignedManagersIds) {
            unassignManager(new Manager(managerId, null, null, null, null));
        }
    }

    public void changeFuelPrice(FuelGrade fuelGrade, float newPrice) {
        FuelPrice oldPrice = fuelPrices.stream()
            .filter(fp -> fp.fuelGrade() == fuelGrade)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Cannot find fuel price with specified fuel grade"));

        fuelPrices.remove(oldPrice);
        fuelPrices.add(new FuelPrice(fuelGrade, newPrice));
        pushDomainEvent(new FuelPriceWasChanged(id));
    }
}