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
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;
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
    @DisplayName("processFuelDelivery Tests")
    class ProcessFuelDeliveryTests {
        
        @Test
        @DisplayName("Should throw exception when fuel order is not confirmed")
        void shouldThrowExceptionWhenFuelOrderIsNotConfirmed() {
            // Given
            FuelOrder pendingOrder = new FuelOrder();
            pendingOrder.setId(1L);
            pendingOrder.setStatus(FuelOrderStatus.PENDING);
            pendingOrder.setGrade(FuelGrade.RON_92);
            pendingOrder.setAmount(1000);

            // When & Then
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> fuelStation.processFuelDelivery(pendingOrder)
            );
            assertEquals("Fuel order must be in Confirmed status to process.", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when there is not enough tank capacity")
        void shouldThrowExceptionWhenNotEnoughTankCapacity() {
            // Given
            FuelOrder confirmedOrder = new FuelOrder();
            confirmedOrder.setId(1L);
            confirmedOrder.setStatus(FuelOrderStatus.CONFIRMED);
            confirmedOrder.setGrade(FuelGrade.RON_92);
            confirmedOrder.setAmount(6000); // Available volume is only 5000

            // When & Then
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> fuelStation.processFuelDelivery(confirmedOrder)
            );
            assertEquals("Not enough tank capacity to process fuel delivery.", exception.getMessage());
        }

        @Test
        @DisplayName("Should process fuel delivery when there is enough capacity in a single tank")
        void shouldProcessFuelDeliveryWithSingleTank() {
            // Given
            FuelOrder confirmedOrder = new FuelOrder();
            confirmedOrder.setId(1L);
            confirmedOrder.setStatus(FuelOrderStatus.CONFIRMED);
            confirmedOrder.setGrade(FuelGrade.RON_92);
            confirmedOrder.setAmount(3000);
            
            LocalDate beforeDelivery = LocalDate.now();

            // When
            fuelStation.processFuelDelivery(confirmedOrder);

            // Then
            FuelTank updatedTank = fuelStation.getFuelTanks().get(0);
            assertEquals(8000, updatedTank.getCurrentVolume());
            assertEquals(2000, updatedTank.getAvailableVolume());
            assertTrue(updatedTank.getLastRefillDate().isPresent());
            assertFalse(updatedTank.getLastRefillDate().get().isBefore(beforeDelivery));
        }

        @Test
        @DisplayName("Should fill multiple tanks when needed")
        void shouldFillMultipleTanksWhenNeeded() {
            // Given
            // Create a new tank with the same fuel grade
            FuelTank additionalTank = new FuelTank(4L, FuelGrade.RON_92, 2000, 7000, Optional.empty());
            fuelStation.getFuelTanks().add(additionalTank);
            
            FuelOrder confirmedOrder = new FuelOrder();
            confirmedOrder.setId(1L);
            confirmedOrder.setStatus(FuelOrderStatus.CONFIRMED);
            confirmedOrder.setGrade(FuelGrade.RON_92);
            confirmedOrder.setAmount(8000); // More than the capacity of the first tank
            
            LocalDate beforeDelivery = LocalDate.now();

            // When
            fuelStation.processFuelDelivery(confirmedOrder);

            // Then
            FuelTank firstTank = fuelStation.getFuelTanks().get(0);
            FuelTank secondTank = fuelStation.getFuelTanks().get(3);
            
            assertEquals(10000, firstTank.getCurrentVolume()); // First tank should be full
            assertEquals(0, firstTank.getAvailableVolume());
            assertTrue(firstTank.getLastRefillDate().isPresent());
            assertFalse(firstTank.getLastRefillDate().get().isBefore(beforeDelivery));
            
            assertEquals(5000, secondTank.getCurrentVolume()); // Second tank should have the remaining fuel
            assertEquals(2000, secondTank.getAvailableVolume());
            assertTrue(secondTank.getLastRefillDate().isPresent());
            assertFalse(secondTank.getLastRefillDate().get().isBefore(beforeDelivery));
        }
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
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> fuelStation.assignManager(manager.getId())
            );
            assertEquals("Manager is already assigned to the fuel station", exception.getMessage()); }
        
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
            fuelStation.unassignManager(manager);
            
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
            fuelStation.unassignManager(manager);
            
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
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> fuelStation.changeFuelPrice(FuelGrade.DIESEL, 4.2f)
            );
            assertEquals("Cannot find fuel price with specified fuel grade", exception.getMessage());
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
