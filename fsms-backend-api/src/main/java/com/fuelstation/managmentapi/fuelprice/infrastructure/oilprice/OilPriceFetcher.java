package com.fuelstation.managmentapi.fuelprice.infrastructure.oilprice;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.common.domain.FuelUnit;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.model.FuelPriceEntity;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.jpa.JpaFuelPriceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
            try {
                updateFuelGrade(fuelGrade, fuelPrices, apiCache);
            } catch (IllegalStateException ex) {
                log.warn("Skipped fuel grade {}: {}", fuelGrade, ex.getMessage());
            } catch (Exception ex) {
                log.error("Unexpected error while updating fuel grade {}", fuelGrade, ex);
            }
        }
    }

    private void updateFuelGrade(
        FuelGrade fuelGrade,
        List<FuelPriceEntity> fuelPrices,
        Map<OilPriceCommodity, OilPriceResponse<OilPriceBenchmark>> apiCache
    ) {
        var latestFuelPrice = fuelPrices.stream()
            .filter(fp -> fp.getFuelGradeId().equals(fuelGrade.getId()))
            .findFirst();

        var hasRefreshIntervalPassed = latestFuelPrice
            .map(fp -> fp.getFetchedAt()
                .isBefore(OffsetDateTime.now(ZoneOffset.UTC).minusHours(REFRESH_INTERVAL_HOURS)))
            .orElse(true);
        if (!hasRefreshIntervalPassed) {
            return;
        }

        var benchmark = apiCache.computeIfAbsent(fuelGradeToCode(fuelGrade), oilPriceClient::getBenchmark);
        var fuelUnit = FuelUnit.fromString(benchmark.getData().getUnit());
        var normalizedPrice = normalizePrice(fuelUnit, benchmark.getData().getPrice());
        var newFuelPrice = FuelPriceEntity.builder()
            .fuelGradeId(fuelGrade.getId())
            .unit(FuelUnit.LITER)
            .price(getPriceWithSpread(fuelGrade, normalizedPrice))
            .currencyCode(CurrencyCode.fromString(benchmark.getData().getCurrency()))
            .source("OilPriceApi")
            .fetchedAt(OffsetDateTime.now(ZoneOffset.UTC))
            .build();

        jpaFuelPriceRepository.save(newFuelPrice);

        log.info("Fetched new fuel price from OilPriceApi for {}", fuelGrade.name());
    }

    private BigDecimal normalizePrice(FuelUnit fuelUnit, BigDecimal price) {
        return switch (fuelUnit) {
            case LITER -> price;
            case GALLON -> price.divide(new BigDecimal("3.78541178"), 6, RoundingMode.HALF_UP);
        };
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
