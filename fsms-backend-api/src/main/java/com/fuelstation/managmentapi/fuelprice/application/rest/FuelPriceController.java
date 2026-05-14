package com.fuelstation.managmentapi.fuelprice.application.rest;

import com.fuelstation.managmentapi.common.domain.CountryCode;
import com.fuelstation.managmentapi.fuelprice.application.query.ListFuelPricesQuery;
import com.fuelstation.managmentapi.fuelprice.application.query.ListTaxedFuelPriceQuery;
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
    private final ListTaxedFuelPriceQuery listTaxedFuelPriceQuery;

    @GetMapping("/countries/{countryCode}/fuel-prices")
    public ResponseEntity<List<TaxedFuelPriceResponse>> getAllFuelPricesByCountryCode(
        @PathVariable CountryCode countryCode,
        @RequestParam(value = "latest", defaultValue = "false") boolean latest
    ) {
        return ResponseEntity.ok(listTaxedFuelPriceQuery.handle(countryCode, latest));
    }

    @GetMapping("/fuel-prices")
    public ResponseEntity<List<FuelPriceResponse>> getFuelPrices(
        @RequestParam(value = "latest", defaultValue = "false") boolean latest
    ) {
        return ResponseEntity.ok(listFuelPricesQuery.handle(latest));
    }

}
