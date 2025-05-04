package com.fuelstation.managmentapi.fuelstation.application.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.application.usecases.CreateFuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase()
@ActiveProfiles("test")
@Transactional
public class GetFuelStationOrdersIntegrationTest {

    @Autowired
    private GetFuelStationOrders getFuelStationOrders;

    @Autowired
    private CreateFuelStation createFuelStation;

    @Autowired
    private CreateFuelOrder createFuelOrder;

    private FuelStation testFuelStation;

    private List<FuelOrder> testFuelOrders = new ArrayList<>();

    @BeforeEach
    public void setup() {
        testFuelStation = createFuelStation.process("", "", "", "", "");
        testFuelOrders.add(createFuelOrder.process(testFuelStation.getId(), FuelGrade.DIESEL, 20));
        testFuelOrders.add(createFuelOrder.process(testFuelStation.getId(), FuelGrade.RON_92, 20));
        testFuelOrders.add(createFuelOrder.process(testFuelStation.getId(), FuelGrade.RON_95, 20));
    }

    @Test
    @DisplayName("Should get fuel station orders")
    public void shouldGetFuelStationOrders() {
        List<FuelOrder> fuelOrders = getFuelStationOrders.process(testFuelStation.getId());

        assertTrue(fuelOrders.size() == testFuelOrders.size());
    }

    @Test
    @DisplayName("Should throw fuel station not found exception")
    public void shouldThrowFuelStationNotFoundException() {
        long nonExistentFuelStationId = 99999l;

        assertThrows(FuelStationNotFoundException.class, () -> {
            getFuelStationOrders.process(nonExistentFuelStationId);
        });
    }
    

    
}
