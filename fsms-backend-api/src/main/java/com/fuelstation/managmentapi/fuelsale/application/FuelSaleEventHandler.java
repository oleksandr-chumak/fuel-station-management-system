package com.fuelstation.managmentapi.fuelsale.application;

import com.fuelstation.managmentapi.fuelsale.application.usecases.CreateFuelSale;
import com.fuelstation.managmentapi.fuelsale.domain.events.FuelSaleCreated;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelTankVolumeChanged;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTankVolumeChangeReason;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class FuelSaleEventHandler {

    private static final Logger log = LoggerFactory.getLogger(FuelSaleEventHandler.class);

    private final CreateFuelSale createFuelSale;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handle(FuelTankVolumeChanged event) {
        if (event.getReason() != FuelTankVolumeChangeReason.DISPENSE) {
            return;
        }

        BigDecimal volumeDispensed = event.getOldVolume().subtract(event.getNewVolume());
        if (volumeDispensed.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        try {
            createFuelSale.process(
                    event.getFuelStationId(),
                    event.getFuelTankId(),
                    event.getFuelGrade(),
                    volumeDispensed,
                    event.getPerformedBy()
            );
        } catch (Exception e) {
            log.error("Failed to create fuel sale for tank {} at station {}: {}",
                    event.getFuelTankId(), event.getFuelStationId(), e.getMessage());
        }
    }

    @EventListener
    public void handle(FuelSaleCreated event) {
        log.info("Fuel sale was created ID:{}", event.getFuelSaleId());
        messagingTemplate.convertAndSend("/topic/fuel-sales", event);
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-sales",
                event
        );
    }
}
