package com.fuelstation.managmentapi.manager.application.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuelstation.managmentapi.manager.application.usecases.CreateManager;
import com.fuelstation.managmentapi.manager.application.usecases.GetAllManagers;
import com.fuelstation.managmentapi.manager.application.usecases.GetManagerById;
import com.fuelstation.managmentapi.manager.application.usecases.TerminateManager;
import com.fuelstation.managmentapi.manager.domain.Manager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    @Autowired
    private CreateManager createManager;
    
    @Autowired
    private TerminateManager terminateManager;

    @Autowired
    private GetAllManagers getAllManagers;

    @Autowired 
    private GetManagerById getManagerById;

    @PostMapping
    public ResponseEntity<ManagerResponse> createManager(@RequestBody CreateManagerRequest request) {
        Manager manager = createManager.process(
            request.getFirstName(),
            request.getLastName(),
            request.getEmail()
        );
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
        return ResponseEntity.ok(ManagerResponse.fromDomain(getManagerById.process(managerId)));
    }
    
}