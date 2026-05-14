package com.fuelstation.managmentapi.fuelprice.application.query;

import com.fuelstation.managmentapi.common.domain.CountryCode;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.fuelprice.application.query.model.FuelPriceResponse;
import com.fuelstation.managmentapi.fuelprice.application.query.model.FuelTaxRuleResponse;
import com.fuelstation.managmentapi.fuelprice.application.query.model.TaxedFuelPriceResponse;
import com.fuelstation.managmentapi.fuelprice.domain.TaxType;
import com.fuelstation.managmentapi.fuelprice.domain.TaxUnit;
import com.fuelstation.managmentapi.fuelprice.infrastructure.exchangerate.ExchangeRateClient;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.FuelPriceRepository;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.FuelTaxRuleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@AllArgsConstructor
public class ListTaxedFuelPriceQuery {

    private final FuelPriceRepository fuelPriceRepository;
    private final FuelTaxRuleRepository fuelTaxRuleRepository;

    private final ExchangeRateClient exchangeRateClient;

    public List<TaxedFuelPriceResponse> handle(CountryCode countryCode, boolean latest) {
        var taxedFuelPrices = new ArrayList<TaxedFuelPriceResponse>();
        var currencyCode = CurrencyCode.fromCountryCode(countryCode);

        var fuelPrices = latest ? fuelPriceRepository.findLatest() : fuelPriceRepository.findAll();
        var fuelTaxRules = fuelTaxRuleRepository.findEffectiveByCountyCode(countryCode);

        for (var fuelPrice : fuelPrices) {
            var sourceToTargetRate = exchangeRateClient.getConversionRate(fuelPrice.currency(), currencyCode);
            var price = fuelPrice.price().multiply(sourceToTargetRate.getConversionRate());

            var fuelGradeTaxRules = fuelTaxRules.stream()
                .filter(rule -> rule.fuelGrade() == fuelPrice.fuelGrade())
                .sorted(Comparator.comparing(rule -> rule.taxType() == TaxType.VAT))
                .toList();

            for (var rule : fuelGradeTaxRules) {
                switch (rule.valueType()) {
                    case FIXED_AMOUNT -> {
                        var taxRateToTargetRate = exchangeRateClient.getConversionRate(rule.currency(), currencyCode);
                        var amountToApply = rule.unit() == TaxUnit.PER_1000_LITERS
                            ? rule.amount().divide(new BigDecimal("1000"), 6, RoundingMode.HALF_UP)
                            : rule.amount();
                        price = price.add(amountToApply.multiply(taxRateToTargetRate.getConversionRate()));
                    }
                    case PERCENTAGE -> {
                        var multiplier = rule.amount()
                            .divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)
                            .add(BigDecimal.ONE);
                        price = price.multiply(multiplier);
                    }
                }
            }

            var priceResponse = new FuelPriceResponse(
                fuelPrice.fuelPriceId(),
                fuelPrice.fuelGrade(),
                fuelPrice.unit(),
                price,
                currencyCode,
                fuelPrice.source(),
                fuelPrice.fetchedAt()
            );
            var taxRuleResponses = fuelGradeTaxRules.stream().map(FuelTaxRuleResponse::from).toList();
            taxedFuelPrices.add(new TaxedFuelPriceResponse(priceResponse, taxRuleResponses));
        }

        return taxedFuelPrices;
    }

}
