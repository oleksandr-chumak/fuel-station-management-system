package com.fuelstation.managmentapi.fuelstation.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.common.domain.Actor;
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

    @BeforeEach
    void setUp() {
        FuelStationAddress address = new FuelStationAddress("Main St", "123", "New York", "10001", "USA");

        List<FuelTank> fuelTanks = new ArrayList<>();
        fuelTanks.add(new FuelTank(1L, FuelGrade.RON_92, BigDecimal.valueOf(5000), BigDecimal.valueOf(10000), Optional.empty()));
        fuelTanks.add(new FuelTank(2L, FuelGrade.RON_95, BigDecimal.valueOf(2000), BigDecimal.valueOf(8000), Optional.empty()));
        fuelTanks.add(new FuelTank(3L, FuelGrade.DIESEL, BigDecimal.valueOf(3000), BigDecimal.valueOf(12000), Optional.empty()));

        List<FuelPrice> fuelPrices = new ArrayList<>();
        fuelPrices.add(new FuelPrice(FuelGrade.RON_92, BigDecimal.valueOf(3.5f)));
        fuelPrices.add(new FuelPrice(FuelGrade.RON_95, BigDecimal.valueOf(4.0f)));
        fuelPrices.add(new FuelPrice(FuelGrade.DIESEL, BigDecimal.valueOf(3.8f)));

        List<Long> assignedManagersIds = new ArrayList<>();
        var createdAt = OffsetDateTime.now();
        
        fuelStation = new FuelStation(
            1L,
                address,
                fuelTanks,
                fuelPrices,
                assignedManagersIds,
                FuelStationStatus.ACTIVE,
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
            Manager manager = new Manager(1L, null, null, null, null, null, null);
            fuelStation.getAssignedManagersIds().add(manager.getManagerId());
            
            // When & Then
            assertThrows(
                ManagerAlreadyAssignedException.class,
                () -> fuelStation.assignManager(manager.getManagerId(), Actor.system())
            );
        }
        
        @Test
        @DisplayName("Should assign a new manager")
        void shouldAssignNewManager() {
            // Given
            Manager newManager = new Manager(2L, null, null, null, null, null, null);
            assertFalse(fuelStation.getAssignedManagersIds().contains(newManager.getManagerId()));

            // When
            fuelStation.assignManager(newManager.getManagerId(), Actor.system());

            // Then
            assertTrue(fuelStation.getAssignedManagersIds().contains(newManager.getManagerId()));
            assertEquals(1, fuelStation.getAssignedManagersIds().size());
        }
        
        @Test
        @DisplayName("Should unassign a manager")
        void shouldUnassignManager() {
            // Given
            Manager manager = new Manager(3L, null, null, null, null, null, null);
            fuelStation.getAssignedManagersIds().add(manager.getManagerId());
            assertEquals(1, fuelStation.getAssignedManagersIds().size());
            
            // When
            fuelStation.unassignManager(manager.getManagerId(), Actor.system());
            
            // Then
            assertEquals(0, fuelStation.getAssignedManagersIds().size());
            assertFalse(fuelStation.getAssignedManagersIds().contains(manager.getManagerId()));
        }
        
        @Test
        @DisplayName("Should do nothing when unassign a manager that isn't assigned")
        void shouldDoNothingWhenUnassigningNonAssignedManager() {
            // Given
            Manager manager = new Manager(4L, null, null, null, null, null, null);
            assertEquals(0, fuelStation.getAssignedManagersIds().size());
            
            // When
            fuelStation.unassignManager(manager.getManagerId(), Actor.system());
            
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
            fuelStation.getFuelPrices().add(new FuelPrice(FuelGrade.RON_92, BigDecimal.valueOf(3.5f)));
            
            // When & Then
            assertThrows(
                FuelGradeNotFoundException.class,
                () -> fuelStation.changeFuelPrice(FuelGrade.DIESEL, BigDecimal.valueOf(4.2f), Actor.system())
            );
        }
        
        @Test
        @DisplayName("Should change fuel price for existing fuel grade")
        void shouldChangeFuelPrice() {
            // Given
            var newPrice = BigDecimal.valueOf(4.5f);
            
            // When
            fuelStation.changeFuelPrice(FuelGrade.RON_95, newPrice, Actor.system());
            
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
