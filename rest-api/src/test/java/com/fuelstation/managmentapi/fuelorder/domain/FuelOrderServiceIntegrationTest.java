package com.fuelstation.managmentapi.fuelorder.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationRepository;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationService;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class FuelOrderServiceIntegrationTest {

    @Autowired
    private FuelOrderService fuelOrderService;

    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    @Autowired
    private FuelStationService fuelStationService;

    @Autowired
    private FuelStationRepository fuelStationRepository;

    private FuelStation testFuelStation;

    @BeforeEach
    void setUp() {
        fuelOrderRepository.deleteAll();
        fuelStationRepository.deleteAll();

        // Create test fuel station with fuel tanks
        testFuelStation = fuelStationService.createFuelStation(
            "Main Street", "123", "New York", "10001", "USA"
        );

        // Add fuel tanks directly to the station
        FuelTank tank = new FuelTank(
            null, 
            FuelGrade.RON_95, 
            500.0f,  // currentVolume
            1000.0f, // maxCapacity
            Optional.of(LocalDate.now())
        );
        testFuelStation.setFuelTanks(List.of(tank));
        testFuelStation = fuelStationRepository.save(testFuelStation);
    }

    @Test
    void testCreateFuelOrderSuccess() {
        FuelOrder order = fuelOrderService.createFuelOrder(
            testFuelStation.getId(), 
            FuelGrade.RON_95, 
            300.0f
        );

        assertNotNull(order.getId());
        assertEquals(FuelOrderStatus.Pending, order.getStatus());
        assertEquals(300.0f, order.getAmount(), 0.001);
        assertEquals(testFuelStation.getId(), order.getFuelStationId());
    }

    @Test
    void testCreateFuelOrderExceedsCapacity() {
        // First valid order
        fuelOrderService.createFuelOrder(testFuelStation.getId(), FuelGrade.RON_95, 300.0f);

        // Second order should fail (500 available - 300 pending = 200 remaining)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fuelOrderService.createFuelOrder(testFuelStation.getId(), FuelGrade.RON_95, 250.0f);
        });

        assertTrue(exception.getMessage().contains("exceeds available tank space"));
    }

    @Test
    void testConfirmFuelOrder() {
        FuelOrder order = fuelOrderService.createFuelOrder(
            testFuelStation.getId(), 
            FuelGrade.RON_95, 
            300.0f
        );

        FuelOrder confirmedOrder = fuelOrderService.confirmFuelOrder(order.getId());
        assertEquals(FuelOrderStatus.Confirmed, confirmedOrder.getStatus());

        // Verify database state
        FuelOrder persistedOrder = fuelOrderRepository.findById(order.getId()).get();
        assertEquals(FuelOrderStatus.Confirmed, persistedOrder.getStatus());
    }

    @Test
    void testRejectFuelOrder() {
        FuelOrder order = fuelOrderService.createFuelOrder(
            testFuelStation.getId(), 
            FuelGrade.RON_95, 
            300.0f
        );

        FuelOrder rejectedOrder = fuelOrderService.rejectFuelOrder(order.getId());
        assertEquals(FuelOrderStatus.Rejected, rejectedOrder.getStatus());

        // Verify database state
        FuelOrder persistedOrder = fuelOrderRepository.findById(order.getId()).get();
        assertEquals(FuelOrderStatus.Rejected, persistedOrder.getStatus());
    }

    @Test
    void testOrderNotFoundConfirming() {
        assertThrows(NoSuchElementException.class, () -> {
            fuelOrderService.confirmFuelOrder(999999L);
        });
    }

    @Test
    void testOrderNotFoundRejecting() {
        assertThrows(NoSuchElementException.class, () -> {
            fuelOrderService.rejectFuelOrder(999999L);
        });
    }

    @Test
    void testPendingOrdersAffectAvailability() {
        // First order consumes most capacity
        fuelOrderService.createFuelOrder(testFuelStation.getId(), FuelGrade.RON_95, 450.0f);

        // Second order should have only 50 available (500 - 450)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fuelOrderService.createFuelOrder(testFuelStation.getId(), FuelGrade.RON_95, 100.0f);
        });

        assertTrue(exception.getMessage().contains("Max allowed: 50.0"));
    }

    @Test
    void testConfirmedOrdersFreeUpCapacity() {
        FuelOrder order = fuelOrderService.createFuelOrder(
            testFuelStation.getId(), 
            FuelGrade.RON_95, 
            450.0f
        );
        fuelOrderService.confirmFuelOrder(order.getId());

        // Should now have 500 - 450 = 50 available (confirmed orders don't count against pending)
        FuelOrder newOrder = fuelOrderService.createFuelOrder(
            testFuelStation.getId(), 
            FuelGrade.RON_95, 
            50.0f
        );
        
        assertNotNull(newOrder);
        assertEquals(50.0f, newOrder.getAmount(), 0.001);
    }
}