package com.fuelstation.managmentapi.fuelstation.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuelstation.managmentapi.fuelstation.application.usecases.AssignManagerToFuelStation;
import com.fuelstation.managmentapi.fuelstation.application.usecases.ChangeFuelPrice;
import com.fuelstation.managmentapi.fuelstation.application.usecases.CreateFuelStation;
import com.fuelstation.managmentapi.fuelstation.application.usecases.DeactivateFuelStation;
import com.fuelstation.managmentapi.fuelstation.application.usecases.UnassignManagerToFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@RestController
@RequestMapping("/api/fuel-stations")
public class FuelStationController {

    @Autowired
    private CreateFuelStation createFuelStation;
    
    @Autowired
    private DeactivateFuelStation deactivateFuelStation;
    
    @Autowired
    private AssignManagerToFuelStation assignManagerToFuelStation;
    
    @Autowired
    private UnassignManagerToFuelStation unassignManagerToFuelStation;
    
    @Autowired
    private ChangeFuelPrice changeFuelPrice;

    @PostMapping
    public ResponseEntity<FuelStationResponse> createFuelStation(@RequestBody CreateFuelStationRequest request) {
        FuelStation fuelStation = createFuelStation.process(
            request.getStreet(),
            request.getBuildingNumber(),
            request.getCity(),
            request.getPostalCode(),
            request.getCountry()
        );
        return new ResponseEntity<>(FuelStationResponse.fromDomain(fuelStation), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<FuelStationResponse> deactivateFuelStation(@PathVariable("id") Long fuelStationId) {
        FuelStation fuelStation = deactivateFuelStation.process(fuelStationId);
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @PutMapping("/{id}/assign-manager")
    public ResponseEntity<FuelStationResponse> assignManager(
            @PathVariable("id") Long fuelStationId,
            @RequestBody AssignManagerRequest request) {
        FuelStation fuelStation = assignManagerToFuelStation.process(fuelStationId, request.getManagerId());
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @PutMapping("/{id}/unassign-manager")
    public ResponseEntity<FuelStationResponse> unassignManager(
            @PathVariable("id") Long fuelStationId,
            @RequestBody AssignManagerRequest request) {
        FuelStation fuelStation = unassignManagerToFuelStation.process(fuelStationId, request.getManagerId());
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @PutMapping("/{id}/change-fuel-price")
    public ResponseEntity<FuelStationResponse> changeFuelPrice(
            @PathVariable("id") Long fuelStationId,
            @RequestBody ChangeFuelPriceRequest request) {
        FuelStation fuelStation = changeFuelPrice.process(
            fuelStationId,
            request.getFuelGrade(),
            request.getNewPrice()
        );
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }
}