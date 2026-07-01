package com.fuelstation.managmentapi.fuelorder.application.rest;

import java.util.List;

import com.fuelstation.managmentapi.authentication.application.CurrentUser;
import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.fuelorder.application.query.GetFuelOrderRecommendedPriceQuery;
import com.fuelstation.managmentapi.fuelorder.application.usecases.command.CreateFuelOrderCommand;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeConfirmedException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeRejectedException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuelstation.managmentapi.fuelorder.application.query.GetFuelOrderByIdQuery;
import com.fuelstation.managmentapi.fuelorder.application.query.ListFuelOrdersQuery;
import com.fuelstation.managmentapi.fuelorder.application.usecases.ConfirmFuelOrder;
import com.fuelstation.managmentapi.fuelorder.application.usecases.CreateFuelOrder;
import com.fuelstation.managmentapi.fuelorder.application.usecases.RejectFuelOrder;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/fuel-orders")
@AllArgsConstructor
public class FuelOrderController {

    private final CreateFuelOrder createFuelOrder;
    private final ConfirmFuelOrder confirmFuelOrder;
    private final RejectFuelOrder rejectFuelOrder;
    private final ListFuelOrdersQuery listFuelOrdersQuery;
    private final GetFuelOrderByIdQuery getFuelOrderByIdQuery;
    private final GetFuelOrderRecommendedPriceQuery getFuelOrderRecommendedPriceQuery;

    @PostMapping("/")
    public ResponseEntity<FuelOrderResponse> createFuelOrder(
        @RequestBody @Valid CreateFuelOrderRequest request,
        @CurrentUser User user
    ) {
        var command = new CreateFuelOrderCommand(
            request.fuelStationId(),
            request.fuelGrade(),
            request.allocations().stream().map(CreateFuelOrderRequest.AllocationRequest::toDomain).toList(),
            user.getActor()
        );
        var fuelOrder = FuelOrderResponse.fromDomain(createFuelOrder.process(command));

        return new ResponseEntity<>(fuelOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<FuelOrderResponse> confirmFuelOrder(
        @PathVariable("id") long fuelOrderId,
        @RequestBody @Valid ConfirmFuelOrderRequest request,
        @CurrentUser User user
    ) {
        try {
            FuelOrder fuelOrder = confirmFuelOrder.process(fuelOrderId, request.pricePerLiter(), user.getActor());
            return ResponseEntity.ok(FuelOrderResponse.fromDomain(fuelOrder));
        } catch (FuelOrderCannotBeConfirmedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<FuelOrderResponse> rejectFuelOrder(
        @PathVariable("id") long fuelOrderId,
        @CurrentUser User user
    ) {
        try {
            FuelOrder fuelOrder = rejectFuelOrder.process(fuelOrderId, user.getActor());
            return ResponseEntity.ok(FuelOrderResponse.fromDomain(fuelOrder));
        } catch (FuelOrderCannotBeRejectedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<FuelOrderResponse>> getFuelOrders() {
        return ResponseEntity.ok(listFuelOrdersQuery.process().stream().map(FuelOrderResponse::fromDomain).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelOrderResponse> getFuelOrderById(@PathVariable("id") long fuelOrderId) {
        return ResponseEntity.ok(FuelOrderResponse.fromDomain(getFuelOrderByIdQuery.process(fuelOrderId)));
    }

    @GetMapping("/{id}/recommended-price")
    public ResponseEntity<FuelOrderRecommendedPriceResponse> getRecommendedPrice(
        @PathVariable("id") long fuelOrderId,
        @CurrentUser User user
    ) {
        var result = getFuelOrderRecommendedPriceQuery.process(fuelOrderId, user.getActor());
        return ResponseEntity.ok(FuelOrderRecommendedPriceResponse.fromResult(result));
    }

}
