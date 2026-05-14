package com.fuelstation.managmentapi.fuelprice.application.rest;

import com.fuelstation.managmentapi.common.domain.CountryCode;
import com.fuelstation.managmentapi.fuelprice.application.query.ListFuelPricesQuery;
import com.fuelstation.managmentapi.fuelprice.application.query.ListLatestFuelPricesQuery;
import com.fuelstation.managmentapi.fuelprice.application.query.ListLatestTaxedFuelPriceQuery;
import com.fuelstation.managmentapi.fuelprice.application.query.model.FuelPriceResponse;
import com.fuelstation.managmentapi.fuelprice.application.query.model.TaxedFuelPriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class FuelPriceController {

    private final ListFuelPricesQuery listFuelPricesQuery;
    private final ListLatestFuelPricesQuery listLatestFuelPricesQuery;
    private final ListLatestTaxedFuelPriceQuery listLatestTaxedFuelPriceQuery;

    @GetMapping("/fuel-prices/latest")
    public ResponseEntity<List<FuelPriceResponse>> getLatestFuelPrices() {
        return ResponseEntity.ok(listLatestFuelPricesQuery.handle());
    }

    @GetMapping("/countries/{countryCode}/fuel-prices/latest")
    public ResponseEntity<List<TaxedFuelPriceResponse>> getLatestFuelPricesByCountryCode(
        @PathVariable CountryCode countryCode
    ) {
        return ResponseEntity.ok(listLatestTaxedFuelPriceQuery.handle(countryCode));
    }

    @GetMapping("/fuel-prices")
    public ResponseEntity<List<FuelPriceResponse>> getFuelPrices() {
        return ResponseEntity.ok(listFuelPricesQuery.handle());
    }

}
