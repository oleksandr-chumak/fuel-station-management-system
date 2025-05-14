package com.fuelstation.managmentapi.fuelstation.application.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderResponse;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.AssignManagerRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.ChangeFuelPriceRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.CreateFuelStationRequest;
import com.fuelstation.managmentapi.fuelstation.application.usecases.AssignManagerToFuelStation;
import com.fuelstation.managmentapi.fuelstation.application.usecases.ChangeFuelPrice;
import com.fuelstation.managmentapi.fuelstation.application.usecases.CreateFuelStation;
import com.fuelstation.managmentapi.fuelstation.application.usecases.DeactivateFuelStation;
import com.fuelstation.managmentapi.fuelstation.application.usecases.GetAllFuelStations;
import com.fuelstation.managmentapi.fuelstation.application.usecases.GetFuelStationById;
import com.fuelstation.managmentapi.fuelstation.application.usecases.GetFuelStationManagers;
import com.fuelstation.managmentapi.fuelstation.application.usecases.GetFuelStationOrders;
import com.fuelstation.managmentapi.fuelstation.application.usecases.UnassignManagerFromFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.manager.application.rest.ManagerResponse;
import com.fuelstation.managmentapi.manager.domain.Manager;

import jakarta.validation.Valid;


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
    private UnassignManagerFromFuelStation unassignManagerFromFuelStation;
    
    @Autowired
    private ChangeFuelPrice changeFuelPrice;
     
    @Autowired
    private GetFuelStationById getFuelStationById;
    
    @Autowired
    private GetAllFuelStations getAllFuelStations;
    
    @Autowired
    private GetFuelStationManagers getFuelStationManagers;
    
    @Autowired
    private GetFuelStationOrders getFuelStationOrders;

    @PostMapping("/")
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
    public ResponseEntity<FuelStationResponse> deactivateFuelStation(@PathVariable("id") long fuelStationId) {
        FuelStation fuelStation = deactivateFuelStation.process(fuelStationId);
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @PutMapping("/{id}/assign-manager")
    public ResponseEntity<FuelStationResponse> assignManager(@PathVariable("id") long fuelStationId,@RequestBody @Valid AssignManagerRequest request) {
        FuelStation fuelStation = assignManagerToFuelStation.process(fuelStationId, request.getManagerId());
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @PutMapping("/{id}/unassign-manager")
    public ResponseEntity<FuelStationResponse> unassignManager(@PathVariable("id") long fuelStationId, @RequestBody AssignManagerRequest request) {
        FuelStation fuelStation = unassignManagerFromFuelStation.process(fuelStationId, request.getManagerId());
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @PutMapping("/{id}/change-fuel-price")
    public ResponseEntity<FuelStationResponse> changeFuelPrice(@PathVariable("id") long fuelStationId, @RequestBody ChangeFuelPriceRequest request) {
        FuelStation fuelStation = changeFuelPrice.process(
            fuelStationId,
            request.getFuelGrade(),
            request.getNewPrice()
        );
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelStationResponse> getFuelStation(@PathVariable("id") long fuelStationId) {
        FuelStation fuelStation = getFuelStationById.process(fuelStationId);
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @GetMapping("/")
    public ResponseEntity<List<FuelStationResponse>> getFuelStations() {
        List<FuelStation> fuelStations = getAllFuelStations.process();
        List<FuelStationResponse> response = fuelStations.stream().map(FuelStationResponse::fromDomain).toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}/managers")
    public ResponseEntity<List<ManagerResponse>> getAssignedManagers(@PathVariable("id") long fuelStationId) {
        List<Manager> managers = getFuelStationManagers.process(fuelStationId);
        List<ManagerResponse> response = managers.stream().map(ManagerResponse::fromDomain).toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}/fuel-orders")
    public ResponseEntity<List<FuelOrderResponse>> getFuelOrders(@PathVariable("id") long fuelStationId) {
        List<FuelOrder> orders = getFuelStationOrders.process(fuelStationId);
        List<FuelOrderResponse> response = orders.stream().map(FuelOrderResponse::fromDomain).toList();
        return ResponseEntity.ok(response);
    }
    
}