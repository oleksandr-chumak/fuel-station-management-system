package com.fuelstation.managmentapi.fuelstation.application.usecases;

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

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase()
@ActiveProfiles("test")
@Transactional
public class GetAllFuelStationsIntegrationTest {

    @Autowired
    private GetAllFuelStations getAllFuelStations;

    @Autowired
    private CreateFuelStation createFuelStation;

    private List<FuelStation> testFuelStations = new ArrayList<>();

    @BeforeEach
    public void setup() { 
        testFuelStations.add(createFuelStation.process("", "", "", "", ""));
        testFuelStations.add(createFuelStation.process("", "", "", "", ""));
        testFuelStations.add(createFuelStation.process("", "", "", "", ""));
    }

    @Test
    @DisplayName("Should get all fuel stations")
    public void shouldGetAllFuelStations() {
        List<FuelStation> fuelStations = getAllFuelStations.process();

        assertTrue(fuelStations.size() == testFuelStations.size());
    }
    
}
