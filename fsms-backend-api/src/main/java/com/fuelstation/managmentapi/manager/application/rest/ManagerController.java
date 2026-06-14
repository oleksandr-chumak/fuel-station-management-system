package com.fuelstation.managmentapi.manager.application.rest;

import java.util.List;

import com.fuelstation.managmentapi.authentication.application.CurrentUser;
import com.fuelstation.managmentapi.authentication.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationResponse;
import com.fuelstation.managmentapi.manager.application.query.GetManagerByIdQuery;
import com.fuelstation.managmentapi.manager.application.query.ListManagerFuelStationsQuery;
import com.fuelstation.managmentapi.manager.application.query.ListManagersQuery;
import com.fuelstation.managmentapi.manager.application.usecases.CreateManager;
import com.fuelstation.managmentapi.manager.application.usecases.TerminateManager;
import com.fuelstation.managmentapi.manager.domain.Manager;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final CreateManager createManager;
    private final TerminateManager terminateManager;
    private final ListManagersQuery listManagersQuery;
    private final ListManagerFuelStationsQuery listManagerFuelStationsQuery;
    private final GetManagerByIdQuery getManagerByIdQuery;

    @PostMapping("/")
    public ResponseEntity<ManagerResponse> createManager(@RequestBody @Valid CreateManagerRequest request) {
        Manager manager = createManager.process(request.getFirstName(), request.getLastName(), request.getEmail());
        return new ResponseEntity<>(ManagerResponse.fromDomain(manager), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/terminate")
    public ResponseEntity<ManagerResponse> terminateManager(
            @PathVariable("id") long managerId,
            @CurrentUser User user
    ) {
        Manager manager = terminateManager.process(managerId, user.getActor());
        return ResponseEntity.ok(ManagerResponse.fromDomain(manager));
    }

    @GetMapping("/")
    public ResponseEntity<List<ManagerResponse>> getManagers() {
        return ResponseEntity.ok(listManagersQuery.process().stream().map(ManagerResponse::fromDomain).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponse> getManagerById(@PathVariable("id") long managerId) {
        return ResponseEntity.ok(ManagerResponse.fromDomain(getManagerByIdQuery.process(managerId)));
    }

    @GetMapping("/{id}/fuel-stations")
    public ResponseEntity<List<FuelStationResponse>> getManagerFuelStations(@PathVariable("id") long managerId) {
        return ResponseEntity.ok(listManagerFuelStationsQuery.process(managerId).stream().map(FuelStationResponse::fromDomain).toList());
    }
    
}