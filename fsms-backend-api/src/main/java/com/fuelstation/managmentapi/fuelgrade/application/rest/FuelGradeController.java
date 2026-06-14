package com.fuelstation.managmentapi.fuelgrade.application.rest;

import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.fuelgrade.application.query.FuelGradeResponse;
import com.fuelstation.managmentapi.fuelgrade.application.rest.response.ListFuelGradesByCountryQuery;
import com.fuelstation.managmentapi.fuelgrade.application.rest.response.ListFuelGradesQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FuelGradeController {

    private final ListFuelGradesQuery listFuelGradesQuery;
    private final ListFuelGradesByCountryQuery listFuelGradesByCountryQuery;

    @GetMapping("/fuel-grades")
    public ResponseEntity<List<FuelGradeResponse>> getFuelGrades() {
        return ResponseEntity.ok(listFuelGradesQuery.handle());
    }

    @GetMapping("/countries/{countryCode}/fuel-grades")
    public ResponseEntity<List<FuelGradeResponse>> getFuelGradesByCountry(
            @PathVariable CountryCode countryCode
    ) {
        return ResponseEntity.ok(listFuelGradesByCountryQuery.handle(countryCode));
    }
}
