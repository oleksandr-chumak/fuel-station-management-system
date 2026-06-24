package com.fuelstation.managmentapi.fuelsale.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelsale.domain.FuelSale;
import com.fuelstation.managmentapi.fuelsale.domain.events.FuelSaleCreated;
import com.fuelstation.managmentapi.fuelsale.infrastructure.persistence.repository.FuelSaleRepository;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationFuelPrice;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Component
@AllArgsConstructor
public class CreateFuelSale {

    private final FuelStationRepository fuelStationRepository;
    private final FuelSaleRepository fuelSaleRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public FuelSale process(
            long fuelStationId,
            long fuelTankId,
            FuelGrade fuelGrade,
            BigDecimal volume,
            Actor performedBy
    ) {
        var station = fuelStationRepository.findById(fuelStationId)
                .orElseThrow(() -> new IllegalStateException("Fuel station not found: " + fuelStationId));

        FuelStationFuelPrice salePrice = station.getFuelPrices().stream()
                .filter(p -> p.fuelGrade() == fuelGrade)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No selling price set for grade " + fuelGrade + " at station " + fuelStationId));

        BigDecimal totalRevenue = volume.multiply(salePrice.pricePerLiter());

        FuelSale sale = new FuelSale(
                null,
                fuelStationId,
                fuelTankId,
                fuelGrade,
                volume,
                salePrice.pricePerLiter(),
                salePrice.currency(),
                totalRevenue,
                OffsetDateTime.now()
        );

        FuelSale saved = fuelSaleRepository.save(sale);
        domainEventPublisher.publish(new FuelSaleCreated(
                saved.getFuelSaleId(), saved.getFuelStationId(), saved.getFuelTankId(), performedBy));
        return saved;
    }
}
