package com.fuelstation.managmentapi.fuelprice.infrastructure.oilprice;

import com.fuelstation.managmentapi.common.domain.Currency;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.common.domain.FuelUnit;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.FuelPriceEntity;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.JpaFuelPriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;

@Component
@EnableScheduling
@AllArgsConstructor
public class OilPriceFetcher {

    private static final int REFRESH_INTERVAL_HOURS = 6;

    private final JpaFuelPriceRepository jpaFuelPriceRepository;
    private final OilPriceClient oilPriceClient;

    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup() {
        runTask();
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void runTask() {
        var apiCache = new HashMap<OilPriceCommodity, OilPriceResponse<OilPriceBenchmark>>();
        var fuelPrices = jpaFuelPriceRepository.findLatest();

        for (var fuelGrade : FuelGrade.values()) {
            var latestFuelPrice = fuelPrices.stream()
                .filter((fp) -> fp.getFuelGradeId().equals(fuelGrade.getId()))
                .findFirst();

            var hasRefreshIntervalPassed = latestFuelPrice
                .map(fp -> fp.getFetchedAt().isBefore(OffsetDateTime.now().minusHours(REFRESH_INTERVAL_HOURS)))
                .orElse(true);
            if(!hasRefreshIntervalPassed) {
                continue;
            }

            var benchmark = apiCache.computeIfAbsent(fuelGradeToCode(fuelGrade), oilPriceClient::getBenchmark);
            var newFuelPrice = FuelPriceEntity.builder()
                .fuelGradeId(fuelGrade.getId())
                .unit(FuelUnit.fromString(benchmark.getData().getUnit()))
                .price(getPriceWithSpread(fuelGrade, benchmark.getData().getPrice()))
                .currency(Currency.fromString(benchmark.getData().getCurrency()))
                .source("OilPriceApi")
                .fetchedAt(OffsetDateTime.now())
                .build();

            jpaFuelPriceRepository.save(newFuelPrice);
        }

    }

    private OilPriceCommodity fuelGradeToCode(FuelGrade fuelGrade) {
        return switch (fuelGrade) {
            case RON_92, RON_95 -> OilPriceCommodity.GASOLINE_RBOB_USD;
            case DIESEL -> OilPriceCommodity.ULSD_DIESEL_USD;
        };
    }

    private BigDecimal getPriceWithSpread(FuelGrade fuelGrade, BigDecimal price) {
        return switch (fuelGrade) {
            case RON_92 -> price.multiply(new BigDecimal("0.97"));
            case DIESEL, RON_95 -> price;
        };
    }

}
