package com.fuelstation.managmentapi.fuelorder.application.rest;

import java.util.List;

import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderAmountExceedsLimitException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeConfirmedException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeRejectedException;
import jakarta.validation.Valid;
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
import com.fuelstation.managmentapi.fuelorder.application.usecases.GetAllFuelOrders;
import com.fuelstation.managmentapi.fuelorder.application.usecases.GetFuelOrderById;
import com.fuelstation.managmentapi.fuelorder.application.usecases.RejectFuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/fuel-orders")
public class FuelOrderController {

    private final CreateFuelOrder createFuelOrder;
    
    private final ConfirmFuelOrder confirmFuelOrder;
    
    private final RejectFuelOrder rejectFuelOrder;

    private final GetAllFuelOrders getAllFuelOrders;

    private final GetFuelOrderById getFuelOrderById;

    public FuelOrderController(CreateFuelOrder createFuelOrder, ConfirmFuelOrder confirmFuelOrder, RejectFuelOrder rejectFuelOrder, GetAllFuelOrders getAllFuelOrders, GetFuelOrderById getFuelOrderById) {
        this.createFuelOrder = createFuelOrder;
        this.confirmFuelOrder = confirmFuelOrder;
        this.rejectFuelOrder = rejectFuelOrder;
        this.getAllFuelOrders = getAllFuelOrders;
        this.getFuelOrderById = getFuelOrderById;
    }

    @PostMapping("/")
    public ResponseEntity<FuelOrderResponse> createFuelOrder(@RequestBody @Valid CreateFuelOrderRequest request) {
        try{
            FuelOrder fuelOrder = createFuelOrder.process(
                    request.getFuelStationId(),
                    request.getFuelGrade(),
                    request.getAmount()
            );

            return new ResponseEntity<>(FuelOrderResponse.fromDomain(fuelOrder), HttpStatus.CREATED);
        } catch (FuelOrderAmountExceedsLimitException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<FuelOrderResponse> confirmFuelOrder(@PathVariable("id") long fuelOrderId) {
        try {
            FuelOrder fuelOrder = confirmFuelOrder.process(fuelOrderId);
            return ResponseEntity.ok(FuelOrderResponse.fromDomain(fuelOrder));
        } catch (FuelOrderCannotBeConfirmedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<FuelOrderResponse> rejectFuelOrder(@PathVariable("id") long fuelOrderId) {
        try {
            FuelOrder fuelOrder = rejectFuelOrder.process(fuelOrderId);
            return ResponseEntity.ok(FuelOrderResponse.fromDomain(fuelOrder));
        } catch (FuelOrderCannotBeRejectedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<FuelOrderResponse>> getFuelOrders() {
        return ResponseEntity.ok(getAllFuelOrders.process().stream().map(FuelOrderResponse::fromDomain).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelOrderResponse> getFuelOrderById(@PathVariable("id") long fuelOrderId) {
        return ResponseEntity.ok(FuelOrderResponse.fromDomain(getFuelOrderById.process(fuelOrderId)));
    }


}