package com.fuelstation.managmentapi.fuelstation.application.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase()
@ActiveProfiles("test")
@Transactional
public class GetFuelStationByIdIntegrationTest {

    @Autowired
    private GetFuelStationById getFuelStationById;

    @Autowired
    private CreateFuelStation createFuelStation;

    private FuelStation testFuelStation;

    @BeforeEach()
    public void setup() { 
        testFuelStation = createFuelStation.process("", "", "", "", "");
    }
    
    @Test
    @DisplayName("Should get fuel station by id")
    public void shouldGetFuelStationById() {
        FuelStation fuelStation = getFuelStationById.process(testFuelStation.getId());

        assertTrue(fuelStation.getId() == testFuelStation.getId());
    }

    @Test
    @DisplayName("Should throw fuel station not found exception")
    public void shouldThrowFuelStationNotFoundException() {
        long nonExistentFuelStationId = 99999l;

        assertThrows(FuelStationNotFoundException.class, () -> {
            getFuelStationById.process(nonExistentFuelStationId);
        });
    }

}
