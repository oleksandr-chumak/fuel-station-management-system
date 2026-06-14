package com.fuelstation.managmentapi.country.application.rest;

import com.fuelstation.managmentapi.country.application.rest.response.ListCountriesQuery;
import com.fuelstation.managmentapi.country.application.query.CountryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CountryController {

    private final ListCountriesQuery listCountriesQuery;

    @GetMapping("/countries")
    public ResponseEntity<List<CountryResponse>> getCountries() {
        return ResponseEntity.ok(listCountriesQuery.handle());
    }
}
