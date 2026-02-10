package com.fuelstation.managmentapi.fuelstation.application.rest;

import java.util.List;

import com.fuelstation.managmentapi.authentication.application.CurrentUser;
import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationAlreadyDeactivatedException;
import lombok.AllArgsConstructor;
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
import com.fuelstation.managmentapi.manager.application.rest.ManagerResponse;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/fuel-stations")
@AllArgsConstructor
public class FuelStationController {

    private final CreateFuelStation createFuelStation;
    private final DeactivateFuelStation deactivateFuelStation;
    private final AssignManagerToFuelStation assignManagerToFuelStation;
    private final UnassignManagerFromFuelStation unassignManagerFromFuelStation;
    private final ChangeFuelPrice changeFuelPrice;
    private final GetFuelStationById getFuelStationById;
    private final GetAllFuelStations getAllFuelStations;
    private final GetFuelStationManagers getFuelStationManagers;
    private final GetFuelStationOrders getFuelStationOrders;

    @PostMapping("/")
    public ResponseEntity<FuelStationResponse> createFuelStation(@RequestBody @Valid CreateFuelStationRequest request) {
        var fuelStation = createFuelStation.process(
            request.getStreet(),
            request.getBuildingNumber(),
            request.getCity(),
            request.getPostalCode(),
            request.getCountry()
        );
        return new ResponseEntity<>(FuelStationResponse.fromDomain(fuelStation), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<FuelStationResponse> deactivateFuelStation(
            @PathVariable("id") long fuelStationId,
            @CurrentUser Credentials credentials
    ) {
        try {
            var fuelStation = deactivateFuelStation.process(fuelStationId, credentials);
            return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
        } catch (FuelStationAlreadyDeactivatedException fuelStationAlreadyDeactivatedException) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, fuelStationAlreadyDeactivatedException.getMessage());
        }
    }

    @PutMapping("/{id}/assign-manager")
    public ResponseEntity<FuelStationResponse> assignManager(
            @PathVariable("id") long fuelStationId,
            @RequestBody @Valid AssignManagerRequest request,
            @CurrentUser Credentials credentials
    ) {
        var fuelStation = assignManagerToFuelStation.process(
                fuelStationId,
                request.getManagerId(),
                credentials
        );
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @PutMapping("/{id}/unassign-manager")
    public ResponseEntity<FuelStationResponse> unassignManager(
            @PathVariable("id") long fuelStationId,
            @RequestBody @Valid AssignManagerRequest request,
            @CurrentUser Credentials credentials
    ) {
        var fuelStation = unassignManagerFromFuelStation.process(
                fuelStationId,
                request.getManagerId(),
                credentials
        );
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @PutMapping("/{id}/change-fuel-price")
    public ResponseEntity<FuelStationResponse> changeFuelPrice(
            @PathVariable("id") long fuelStationId,
            @RequestBody @Valid ChangeFuelPriceRequest request,
            @CurrentUser Credentials credentials
    ) {
        var fuelStation = changeFuelPrice.process(
            fuelStationId,
            request.getFuelGrade(),
            request.getNewPrice(),
            credentials
        );
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelStationResponse> getFuelStation(
            @PathVariable("id") long fuelStationId,
            @CurrentUser Credentials credentials
    ) {
        var fuelStation = getFuelStationById.process(fuelStationId, credentials);
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @GetMapping("/")
    public ResponseEntity<List<FuelStationResponse>> getFuelStations() {
        var fuelStations = getAllFuelStations.process();
        var response = fuelStations.stream().map(FuelStationResponse::fromDomain).toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}/managers")
    public ResponseEntity<List<ManagerResponse>> getAssignedManagers(
            @PathVariable("id") long fuelStationId,
            @CurrentUser Credentials credentials
    ) {
        var managers = getFuelStationManagers.process(fuelStationId, credentials);
        var response = managers.stream().map(ManagerResponse::fromDomain).toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}/fuel-orders")
    public ResponseEntity<List<FuelOrderResponse>> getFuelOrders(
            @PathVariable("id") long fuelStationId,
            @CurrentUser Credentials credentials
    ) {
        List<FuelOrder> orders = getFuelStationOrders.process(fuelStationId, credentials);
        List<FuelOrderResponse> response = orders.stream().map(FuelOrderResponse::fromDomain).toList();
        return ResponseEntity.ok(response);
    }
    
}