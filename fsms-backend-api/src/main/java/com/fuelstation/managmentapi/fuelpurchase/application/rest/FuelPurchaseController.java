package com.fuelstation.managmentapi.fuelpurchase.application.rest;

import com.fuelstation.managmentapi.fuelpurchase.infrastructure.persistence.repository.FuelPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fuel-stations/{stationId}/fuel-purchases")
@RequiredArgsConstructor
public class FuelPurchaseController {

    private final FuelPurchaseRepository fuelPurchaseRepository;

    @GetMapping
    public List<FuelPurchaseResponse> getByStation(@PathVariable long stationId) {
        return fuelPurchaseRepository.findByFuelStationId(stationId).stream()
                .map(FuelPurchaseResponse::from)
                .toList();
    }
}
