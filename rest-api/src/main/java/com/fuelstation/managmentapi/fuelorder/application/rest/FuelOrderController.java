package com.fuelstation.managmentapi.fuelorder.application.rest;

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

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.application.usecases.ConfirmFuelOrder;
import com.fuelstation.managmentapi.fuelorder.application.usecases.CreateFuelOrder;
import com.fuelstation.managmentapi.fuelorder.application.usecases.GetAllFuelOrders;
import com.fuelstation.managmentapi.fuelorder.application.usecases.GetFuelOrderById;
import com.fuelstation.managmentapi.fuelorder.application.usecases.RejectFuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/fuel-orders")
public class FuelOrderController {

    @Autowired
    private CreateFuelOrder createFuelOrder;
    
    @Autowired
    private ConfirmFuelOrder confirmFuelOrder;
    
    @Autowired
    private RejectFuelOrder rejectFuelOrder;

    @Autowired
    private GetAllFuelOrders getAllFuelOrders;

    @Autowired
    private GetFuelOrderById getFuelOrderById;

    @PostMapping("/")
    public ResponseEntity<FuelOrderResponse> createFuelOrder(@RequestBody CreateFuelOrderRequest request) {
        // TODO replace fuel grade
        FuelOrder fuelOrder = createFuelOrder.process(
            request.getFuelStationId(),
            FuelGrade.DIESEL,
            request.getAmount()
        );
        return new ResponseEntity<>(FuelOrderResponse.fromDomain(fuelOrder), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<FuelOrderResponse> confirmFuelOrder(@PathVariable("id") long fuelOrderId) {
        FuelOrder fuelOrder = confirmFuelOrder.process(fuelOrderId);
        return ResponseEntity.ok(FuelOrderResponse.fromDomain(fuelOrder));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<FuelOrderResponse> rejectFuelOrder(@PathVariable("id") long fuelOrderId) {
        FuelOrder fuelOrder = rejectFuelOrder.process(fuelOrderId);
        return ResponseEntity.ok(FuelOrderResponse.fromDomain(fuelOrder));
    }

    @GetMapping("/")
    public ResponseEntity<List<FuelOrderResponse>> getFuelOrders() {
        return ResponseEntity.ok(getAllFuelOrders.process().stream().map(FuelOrderResponse::fromDomain).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelOrderResponse> getMethodName(@PathVariable("id") long fuelOrderId) {
        return ResponseEntity.ok(FuelOrderResponse.fromDomain(getFuelOrderById.process(fuelOrderId)));
    }
    
    
}