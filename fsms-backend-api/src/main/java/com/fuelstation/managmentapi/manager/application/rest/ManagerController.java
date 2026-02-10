package com.fuelstation.managmentapi.manager.application.rest;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationResponse;
import com.fuelstation.managmentapi.manager.application.usecases.CreateManager;
import com.fuelstation.managmentapi.manager.application.usecases.GetAllManagers;
import com.fuelstation.managmentapi.manager.application.usecases.GetManagerByCredentialsId;
import com.fuelstation.managmentapi.manager.application.usecases.GetManagerFuelStations;
import com.fuelstation.managmentapi.manager.application.usecases.TerminateManager;
import com.fuelstation.managmentapi.manager.domain.Manager;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private final CreateManager createManager;
    
    private final TerminateManager terminateManager;

    private final GetAllManagers getAllManagers;

    private final GetManagerFuelStations getManagerFuelStations;

    private final GetManagerByCredentialsId getManagerByCredentialsId;

    public ManagerController(CreateManager createManager, TerminateManager terminateManager, GetAllManagers getAllManagers, GetManagerFuelStations getManagerFuelStations, GetManagerByCredentialsId getManagerByCredentialsId) {
        this.createManager = createManager;
        this.terminateManager = terminateManager;
        this.getAllManagers = getAllManagers;
        this.getManagerFuelStations = getManagerFuelStations;
        this.getManagerByCredentialsId = getManagerByCredentialsId;
    }

    @PostMapping("/")
    public ResponseEntity<ManagerResponse> createManager(@RequestBody @Valid CreateManagerRequest request) {
        Manager manager = createManager.process(request.getFirstName(), request.getLastName(), request.getEmail());
        return new ResponseEntity<>(ManagerResponse.fromDomain(manager), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/terminate")
    public ResponseEntity<ManagerResponse> terminateManager(@PathVariable("id") long managerId) {
        Manager manager = terminateManager.process(managerId);
        return ResponseEntity.ok(ManagerResponse.fromDomain(manager));
    }

    @GetMapping("/")
    public ResponseEntity<List<ManagerResponse>> getManagers() {
        return ResponseEntity.ok(getAllManagers.process().stream().map(ManagerResponse::fromDomain).toList()); 
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponse> getManagerById(@PathVariable("id") long managerId) {
        return ResponseEntity.ok(ManagerResponse.fromDomain(getManagerByCredentialsId.process(managerId)));
    }

    @GetMapping("/{id}/fuel-stations")
    public ResponseEntity<List<FuelStationResponse>> getManagerFuelStations(@PathVariable("id") long managerId) {
        return ResponseEntity.ok(getManagerFuelStations.process(managerId).stream().map(FuelStationResponse::fromDomain).toList());
    }
    
}