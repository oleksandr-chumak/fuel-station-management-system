package com.fuelstation.managmentapi.fuelprice.application.query;

import com.fuelstation.managmentapi.fuelprice.application.query.model.FuelPriceResponse;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.FuelPriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListFuelPricesQuery {

    private final FuelPriceRepository fuelPriceRepository;

    public List<FuelPriceResponse> handle() {
        return fuelPriceRepository.findAll().stream().map(FuelPriceResponse::from).toList();
    }
}
