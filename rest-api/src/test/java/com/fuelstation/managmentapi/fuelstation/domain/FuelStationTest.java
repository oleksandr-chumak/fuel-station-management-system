package com.fuelstation.managmentapi.fuelstation.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelGradeNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.ManagerAlreadyAssignedException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelPrice;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationAddress;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTank;
import com.fuelstation.managmentapi.manager.domain.Manager;

@ExtendWith(MockitoExtension.class)
public class FuelStationTest {

    private FuelStation fuelStation;
    private FuelStationAddress address;
    private List<FuelTank> fuelTanks;
    private List<FuelPrice> fuelPrices;
    private List<Long> assignedManagersIds;
    private LocalDate createdAt;

    @BeforeEach
    void setUp() {
        address = new FuelStationAddress("Main St", "123", "New York", "10001", "USA");
        
        fuelTanks = new ArrayList<>();
        fuelTanks.add(new FuelTank(1L, FuelGrade.RON_92, 5000, 10000, Optional.empty()));
        fuelTanks.add(new FuelTank(2L, FuelGrade.RON_95, 2000, 8000, Optional.empty()));
        fuelTanks.add(new FuelTank(3L, FuelGrade.DIESEL, 3000, 12000, Optional.empty()));
        
        fuelPrices = new ArrayList<>();
        fuelPrices.add(new FuelPrice(FuelGrade.RON_92, 3.5f));
        fuelPrices.add(new FuelPrice(FuelGrade.RON_95, 4.0f));
        fuelPrices.add(new FuelPrice(FuelGrade.DIESEL, 3.8f));
        
        assignedManagersIds = new ArrayList<>();
        createdAt = LocalDate.now();
        
        fuelStation = new FuelStation(
            1L,
            address,
            fuelTanks,
            fuelPrices,
            assignedManagersIds,
            FuelStationStatus.Active,
            createdAt
        );
    }

    @Nested
    @DisplayName("Manager Assignment Tests")
    class ManagerAssignmentTests {
        
        @Test
        @DisplayName("Should throw exception when trying to assign an already assigned manager")
        void shouldThrowExceptionWhenAssigningAlreadyAssignedManager() {
            // Given
            Manager manager = new Manager(1L, null, null, null, null);
            fuelStation.getAssignedManagersIds().add(manager.getId());
            
            // When & Then
            assertThrows(
                ManagerAlreadyAssignedException.class,
                () -> fuelStation.assignManager(manager.getId())
            );
        }
        
        @Test
        @DisplayName("Should assign a new manager")
        void shouldAssignNewManager() {
            // Given
            Manager newManager = new Manager(2L, null, null, null, null);
            assertFalse(fuelStation.getAssignedManagersIds().contains(newManager.getId()));

            // When
            fuelStation.assignManager(newManager.getId());

            // Then
            assertTrue(fuelStation.getAssignedManagersIds().contains(newManager.getId()));
            assertEquals(1, fuelStation.getAssignedManagersIds().size());
        }
        
        @Test
        @DisplayName("Should unassign a manager")
        void shouldUnassignManager() {
            // Given
            Manager manager = new Manager(3L, null, null, null, null);
            fuelStation.getAssignedManagersIds().add(manager.getId());
            assertEquals(1, fuelStation.getAssignedManagersIds().size());
            
            // When
            fuelStation.unassignManager(manager.getId());
            
            // Then
            assertEquals(0, fuelStation.getAssignedManagersIds().size());
            assertFalse(fuelStation.getAssignedManagersIds().contains(manager.getId()));
        }
        
        @Test
        @DisplayName("Should do nothing when unassign a manager that isn't assigned")
        void shouldDoNothingWhenUnassigningNonAssignedManager() {
            // Given
            Manager manager = new Manager(4L, null, null, null, null);
            assertEquals(0, fuelStation.getAssignedManagersIds().size());
            
            // When
            fuelStation.unassignManager(manager.getId());
            
            // Then
            assertEquals(0, fuelStation.getAssignedManagersIds().size());
        }
    }
    
    @Nested
    @DisplayName("Change Fuel Price Tests")
    class ChangeFuelPriceTests {
        
        @Test
        @DisplayName("Should throw exception when fuel grade doesn't exist")
        void shouldThrowExceptionWhenFuelGradeDoesntExist() {
            // First, clear fuel prices and add only one
            fuelStation.getFuelPrices().clear();
            fuelStation.getFuelPrices().add(new FuelPrice(FuelGrade.RON_92, 3.5f));
            
            // When & Then
            assertThrows(
                FuelGradeNotFoundException.class,
                () -> fuelStation.changeFuelPrice(FuelGrade.DIESEL, 4.2f)
            );
        }
        
        @Test
        @DisplayName("Should change fuel price for existing fuel grade")
        void shouldChangeFuelPrice() {
            // Given
            float newPrice = 4.5f;
            
            // When
            fuelStation.changeFuelPrice(FuelGrade.RON_95, newPrice);
            
            // Then
            FuelPrice updatedPrice = fuelStation.getFuelPrices().stream()
                .filter(fp -> fp.fuelGrade() == FuelGrade.RON_95)
                .findFirst()
                .orElseThrow();
                
            assertEquals(newPrice, updatedPrice.pricePerLiter());
            assertEquals(3, fuelStation.getFuelPrices().size()); // Total count should remain the same
        }
    }
}
