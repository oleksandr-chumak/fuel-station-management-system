package com.fuelstation.managmentapi.fuelstation.domain.models;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;
import com.fuelstation.managmentapi.manager.domain.Manager;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FuelStation {  
    private Long id;
    private FuelStationAddress address;
    private List<FuelTank> fuelTanks;
    private List<FuelPrice> fuelPrices;
    private List<Long> assignedManagersIds;
    private FuelStationStatus status;
    private LocalDate createdAt;

    public void processFuelDelivery(FuelOrder fuelOrder) {
        if (fuelOrder.getStatus() != FuelOrderStatus.Confirmed) {
            throw new IllegalStateException("Fuel order must be in Confirmed status to process.");
        }
    
        List<FuelTank> fuelTanksWithSpecifiedFuelGrade = fuelTanks.stream().filter(ft -> ft.getFuelGrade() == fuelOrder.getGrade()).toList();
    
        double totalAvailableAmount = fuelTanksWithSpecifiedFuelGrade.stream().mapToDouble(FuelTank::getAvailableVolume).sum();
    
        if (totalAvailableAmount < fuelOrder.getAmount()) {
            throw new IllegalStateException("Not enough tank capacity to process fuel delivery.");
        }
    
        float remainingFuel = fuelOrder.getAmount();
        for (FuelTank fuelTank : fuelTanksWithSpecifiedFuelGrade) {
            boolean tankHasEnoughSpaceForAllRemainingFuel = (fuelTank.getAvailableVolume() - remainingFuel) >= 0;
    
            if (tankHasEnoughSpaceForAllRemainingFuel) {
                fuelTank.setCurrentVolume(fuelTank.getCurrentVolume() + remainingFuel);
                fuelTank.setLastRefilDate(Optional.of(LocalDate.now()));
                return;
            } else {
                remainingFuel -= fuelTank.getAvailableVolume();
                fuelTank.setCurrentVolume(fuelTank.getMaxCapacity());
                fuelTank.setLastRefilDate(Optional.of(LocalDate.now()));
            }
        }
    }

    public void assignManager(Manager manager) {
        Optional<Long> managerId = assignedManagersIds.stream().filter((id) -> id == manager.getId()).findFirst();

        if(managerId.isPresent()) {
            throw new IllegalArgumentException("Manager is already assigend to the fuel station");
        }

        assignedManagersIds.add(manager.getId()); 
    }

    public void unassignManager(Manager manager) {
        assignedManagersIds.removeIf((id) -> id == manager.getId());
    }

    public void changeFuelPrice(FuelGrade fuelGrade, float newPrice) {
        FuelPrice oldPrice = fuelPrices.stream()
            .filter(fp -> fp.fuelGrade() == fuelGrade)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Cannot find fuel price with specified fuel grade"));

        fuelPrices.remove(oldPrice);
        fuelPrices.add(new FuelPrice(fuelGrade, newPrice));
    }
}