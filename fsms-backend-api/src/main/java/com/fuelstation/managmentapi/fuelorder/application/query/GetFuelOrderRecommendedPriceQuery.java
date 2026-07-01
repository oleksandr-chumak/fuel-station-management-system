package com.fuelstation.managmentapi.fuelorder.application.query;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.FuelUnit;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelprice.application.query.ListTaxedFuelPriceQuery;
import com.fuelstation.managmentapi.fuelstation.application.query.GetFuelStationByIdQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class GetFuelOrderRecommendedPriceQuery {

    private final GetFuelOrderByIdQuery getFuelOrderByIdQuery;
    private final GetFuelStationByIdQuery getFuelStationByIdQuery;
    private final ListTaxedFuelPriceQuery listTaxedFuelPriceQuery;

    public Result process(long fuelOrderId, Actor performedBy) {
        FuelOrder order = getFuelOrderByIdQuery.process(fuelOrderId);
        var station = getFuelStationByIdQuery.process(order.getFuelStationId(), performedBy);
        var country = station.getAddress().country();
        var currency = CurrencyCode.fromCountryCode(country);

        BigDecimal pricePerLiter = listTaxedFuelPriceQuery.handle(country, true).stream()
            .filter(t -> t.fuelPrice().fuelGrade() == order.getGrade()
                && t.fuelPrice().unit() == FuelUnit.LITER)
            .map(t -> t.fuelPrice().price())
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "No taxed price found for grade " + order.getGrade() + " in country " + country
            ));

        return new Result(pricePerLiter, currency, order.getGrade());
    }

    public record Result(BigDecimal pricePerLiter, CurrencyCode currency, FuelGrade fuelGrade) {
    }
}
