package com.fuelstation.managmentapi.fuelorder.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuelstation.managmentapi.fuelorder.application.usecases.ConfirmFuelOrder;
import com.fuelstation.managmentapi.fuelorder.application.usecases.CreateFuelOrder;
import com.fuelstation.managmentapi.fuelorder.application.usecases.RejectFuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;

@RestController
@RequestMapping("/api/fuel-orders")
public class FuelOrderController {

    @Autowired
    private CreateFuelOrder createFuelOrder;
    
    @Autowired
    private ConfirmFuelOrder confirmFuelOrder;
    
    @Autowired
    private RejectFuelOrder rejectFuelOrder;

    @PostMapping
    public ResponseEntity<FuelOrderResponse> createFuelOrder(@RequestBody CreateFuelOrderRequest request) {
        FuelOrder fuelOrder = createFuelOrder.process(
            request.getGasStationId(),
            request.getFuelGrade(),
            request.getAmount()
        );
        return new ResponseEntity<>(FuelOrderResponse.fromDomain(fuelOrder), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<FuelOrderResponse> confirmFuelOrder(@PathVariable("id") Long fuelOrderId) {
        FuelOrder fuelOrder = confirmFuelOrder.process(fuelOrderId);
        return ResponseEntity.ok(FuelOrderResponse.fromDomain(fuelOrder));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<FuelOrderResponse> rejectFuelOrder(@PathVariable("id") Long fuelOrderId) {
        FuelOrder fuelOrder = rejectFuelOrder.process(fuelOrderId);
        return ResponseEntity.ok(FuelOrderResponse.fromDomain(fuelOrder));
    }
}