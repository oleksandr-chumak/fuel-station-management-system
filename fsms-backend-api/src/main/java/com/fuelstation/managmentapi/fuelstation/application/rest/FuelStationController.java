package com.fuelstation.managmentapi.fuelstation.application.rest;

import java.time.Instant;
import java.util.List;

import com.fuelstation.managmentapi.authentication.application.CurrentUser;
import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.common.application.CursorPage;
import com.fuelstation.managmentapi.common.application.DomainEventResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderResponse;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelstation.application.command.ChangeFuelPriceCommand;
import com.fuelstation.managmentapi.fuelstation.application.command.ChangeFuelPricesBulkCommand;
import com.fuelstation.managmentapi.fuelstation.application.query.GetFuelStationByIdQuery;
import com.fuelstation.managmentapi.fuelstation.application.query.ListFuelPriceHistoryQuery;
import com.fuelstation.managmentapi.fuelstation.application.query.ListFuelStationEventsQuery;
import com.fuelstation.managmentapi.fuelstation.application.query.ListFuelStationManagersQuery;
import com.fuelstation.managmentapi.fuelstation.application.query.ListFuelStationOrdersQuery;
import com.fuelstation.managmentapi.fuelstation.application.query.ListFuelStationsQuery;
import com.fuelstation.managmentapi.fuelstation.application.query.ListFuelTankVolumeHistoryQuery;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.AssignManagerRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.CreateFuelStationRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.ChangeFuelPriceRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.ChangeFuelPricesBulkRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.DispenseFuelRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.InstallFuelTankRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.response.FuelPriceHistoryResponse;
import com.fuelstation.managmentapi.fuelstation.application.rest.response.FuelTankVolumeHistoryResponse;
import com.fuelstation.managmentapi.fuelstation.application.usecases.AssignManagerToFuelStation;
import com.fuelstation.managmentapi.fuelstation.application.usecases.ChangeFuelPrice;
import com.fuelstation.managmentapi.fuelstation.application.usecases.ChangeFuelPricesBulk;
import com.fuelstation.managmentapi.fuelstation.application.usecases.CreateFuelStation;
import com.fuelstation.managmentapi.fuelstation.application.usecases.DeactivateFuelStation;
import com.fuelstation.managmentapi.fuelstation.application.usecases.DecommissionFuelTank;
import com.fuelstation.managmentapi.fuelstation.application.usecases.DispenseFuel;
import com.fuelstation.managmentapi.fuelstation.application.usecases.InstallFuelTank;
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
    private final ChangeFuelPricesBulk changeFuelPricesBulk;
    private final DispenseFuel dispenseFuel;
    private final DecommissionFuelTank decommissionFuelTank;
    private final InstallFuelTank installFuelTank;

    private final GetFuelStationByIdQuery getFuelStationByIdQuery;
    private final ListFuelStationsQuery listFuelStationsQuery;
    private final ListFuelStationManagersQuery listFuelStationManagersQuery;
    private final ListFuelStationOrdersQuery listFuelStationOrdersQuery;
    private final ListFuelStationEventsQuery listFuelStationEventsQuery;
    private final ListFuelPriceHistoryQuery listFuelPriceHistoryQuery;
    private final ListFuelTankVolumeHistoryQuery listFuelTankVolumeHistoryQuery;

    @PostMapping("/")
    public ResponseEntity<FuelStationResponse> createFuelStation(
            @RequestBody @Valid CreateFuelStationRequest request,
            @CurrentUser User user
            ) {
        var fuelStation = createFuelStation.process(
            request.getStreet(),
            request.getBuildingNumber(),
            request.getCity(),
            request.getPostalCode(),
            request.getCountry(),
            user.getActor()
        );
        return new ResponseEntity<>(FuelStationResponse.fromDomain(fuelStation), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<FuelStationResponse> deactivateFuelStation(
            @PathVariable("id") long fuelStationId,
            @CurrentUser User user
    ) {
        try {
            var fuelStation = deactivateFuelStation.process(fuelStationId, user.getActor());
            return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
        } catch (FuelStationAlreadyDeactivatedException fuelStationAlreadyDeactivatedException) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, fuelStationAlreadyDeactivatedException.getMessage());
        }
    }

    @PutMapping("/{id}/assign-manager")
    public ResponseEntity<ManagerResponse> assignManager(
            @PathVariable("id") long fuelStationId,
            @RequestBody @Valid AssignManagerRequest request,
            @CurrentUser User user
    ) {
        var manager = assignManagerToFuelStation.process(
                fuelStationId,
                request.getManagerId(),
                user.getActor()
        );
        return ResponseEntity.ok(ManagerResponse.fromDomain(manager));
    }

    @PutMapping("/{id}/unassign-manager")
    public ResponseEntity<ManagerResponse> unassignManager(
            @PathVariable("id") long fuelStationId,
            @RequestBody @Valid AssignManagerRequest request,
            @CurrentUser User user
    ) {
        var manager = unassignManagerFromFuelStation.process(
                fuelStationId,
                request.getManagerId(),
                user.getActor()
        );
        return ResponseEntity.ok(ManagerResponse.fromDomain(manager));
    }

    @PutMapping("/{id}/fuel-prices/{fuelGrade}")
    public ResponseEntity<FuelStationResponse> updateFuelPrice(
            @PathVariable("id") long fuelStationId,
            @PathVariable("fuelGrade") FuelGrade fuelGrade,
            @RequestBody @Valid ChangeFuelPriceRequest request,
            @CurrentUser User user
    ) {
        var fuelStation = changeFuelPrice.process(new ChangeFuelPriceCommand(
                fuelStationId,
                fuelGrade,
                request.getNewPrice(),
                user.getActor()
        ));
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @PutMapping("/{id}/fuel-prices")
    public ResponseEntity<FuelStationResponse> updateFuelPrices(
            @PathVariable("id") long fuelStationId,
            @RequestBody @Valid ChangeFuelPricesBulkRequest request,
            @CurrentUser User user
    ) {
        var updates = request.getPrices().stream()
                .map(p -> new ChangeFuelPricesBulkCommand.FuelPriceChange(p.getFuelGrade(), p.getNewPrice()))
                .toList();
        var fuelStation = changeFuelPricesBulk.process(new ChangeFuelPricesBulkCommand(fuelStationId, updates, user.getActor()));
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelStationResponse> getFuelStation(
            @PathVariable("id") long fuelStationId,
            @CurrentUser User user
    ) {
        var fuelStation = getFuelStationByIdQuery.process(fuelStationId, user.getActor());
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @GetMapping("/")
    public ResponseEntity<List<FuelStationResponse>> getFuelStations() {
        var fuelStations = listFuelStationsQuery.process();
        var response = fuelStations.stream().map(FuelStationResponse::fromDomain).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/managers")
    public ResponseEntity<List<ManagerResponse>> getAssignedManagers(
            @PathVariable("id") long fuelStationId,
            @CurrentUser User user
    ) {
        var managers = listFuelStationManagersQuery.process(fuelStationId, user.getActor());
        var response = managers.stream().map(ManagerResponse::fromDomain).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/fuel-orders")
    public ResponseEntity<List<FuelOrderResponse>> getFuelOrders(
            @PathVariable("id") long fuelStationId,
            @CurrentUser User user
    ) {
        List<FuelOrder> orders = listFuelStationOrdersQuery.process(fuelStationId, user.getActor());
        List<FuelOrderResponse> response = orders.stream().map(FuelOrderResponse::fromDomain).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<CursorPage<DomainEventResponse, Instant>> getFuelStationEvents(
            @PathVariable("id") long fuelStationId,
            @RequestParam(required = false) Instant occurredAfter,
            @RequestParam(defaultValue = "10") short limit
    ) {
        return ResponseEntity.ok(listFuelStationEventsQuery.process(fuelStationId, occurredAfter, limit));
    }

    @GetMapping("/{id}/fuel-price-history")
    public ResponseEntity<List<FuelPriceHistoryResponse>> getFuelPriceHistory(
            @PathVariable("id") long fuelStationId
    ) {
        return ResponseEntity.ok(listFuelPriceHistoryQuery.process(fuelStationId));
    }

    @PostMapping("/{id}/fuel-tanks")
    public ResponseEntity<FuelStationResponse> installFuelTank(
            @PathVariable("id") long fuelStationId,
            @RequestBody @Valid InstallFuelTankRequest request,
            @CurrentUser User user
    ) {
        var fuelStation = installFuelTank.process(
                fuelStationId,
                request.getFuelGrade(),
                request.getMaxCapacity(),
                user.getActor()
        );
        return new ResponseEntity<>(FuelStationResponse.fromDomain(fuelStation), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/fuel-tanks/{tankId}/dispense")
    public ResponseEntity<FuelStationResponse> dispenseFuel(
            @PathVariable("id") long fuelStationId,
            @PathVariable("tankId") long fuelTankId,
            @RequestBody @Valid DispenseFuelRequest request,
            @CurrentUser User user
    ) {
        var fuelStation = dispenseFuel.process(fuelStationId, fuelTankId, request.getVolume(), user.getActor());
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @PutMapping("/{id}/fuel-tanks/{tankId}/decommission")
    public ResponseEntity<FuelStationResponse> decommissionFuelTank(
            @PathVariable("id") long fuelStationId,
            @PathVariable("tankId") long fuelTankId,
            @CurrentUser User user
    ) {
        var fuelStation = decommissionFuelTank.process(fuelStationId, fuelTankId, user.getActor());
        return ResponseEntity.ok(FuelStationResponse.fromDomain(fuelStation));
    }

    @GetMapping("/{id}/fuel-tank-volume-history")
    public ResponseEntity<List<FuelTankVolumeHistoryResponse>> getFuelTankVolumeHistory(
            @PathVariable("id") long fuelStationId
    ) {
        return ResponseEntity.ok(listFuelTankVolumeHistoryQuery.process(fuelStationId));
    }

}
