package com.fuelstation.managmentapi.fuelsale.application.rest;

import com.fuelstation.managmentapi.fuelsale.infrastructure.persistence.repository.FuelSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fuel-stations/{stationId}/fuel-sales")
@RequiredArgsConstructor
public class FuelSaleController {

    private final FuelSaleRepository fuelSaleRepository;

    @GetMapping
    public List<FuelSaleResponse> getByStation(@PathVariable long stationId) {
        return fuelSaleRepository.findByFuelStationId(stationId).stream()
                .map(FuelSaleResponse::from)
                .toList();
    }
}
