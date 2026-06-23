package com.fuelstation.managmentapi.fuelstation.application.stomp;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.application.usecases.CreateFuelStationEvent;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelPriceChanged;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationCreated;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationDeactivated;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelTankDecommissioned;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelTankInstalled;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelTankVolumeChanged;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerAssignedToFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.events.ManagerUnassignedFromFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTankVolumeHistory;
import com.fuelstation.managmentapi.fuelstation.infrastructure.FuelStationEmailService;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationFuelPriceHistoryEntity;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationFuelPriceHistoryRepository;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelTankVolumeHistoryRepository;

import java.time.ZoneOffset;

@Component
@AllArgsConstructor
public class FuelStationEventHandler {

    private final Logger logger = LoggerFactory.getLogger(FuelStationEventHandler.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final FuelStationEmailService fuelStationEmailService;
    private final CreateFuelStationEvent createFuelStationEvent;
    private final FuelStationFuelPriceHistoryRepository fuelPriceHistoryRepository;
    private final FuelTankVolumeHistoryRepository fuelTankVolumeHistoryRepository;

    @EventListener
    public void handle(FuelStationCreated event) {
        logger.info("Fuel station was created ID:{}", event.getFuelStationId());
        createFuelStationEvent.process(event);
        messagingTemplate.convertAndSend("/topic/fuel-stations/created", event);
    }

    @EventListener
    public void handle(FuelPriceChanged event) {
        logger.info("Fuel price was changed ID:{}", event.getFuelStationId());
        createFuelStationEvent.process(event);
        savePriceHistory(event);
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-price-changed",
                event
        );
    }

    private void savePriceHistory(FuelPriceChanged event) {
        var entry = new FuelStationFuelPriceHistoryEntity();
        entry.setFuelStationId(event.getFuelStationId());
        entry.setFuelGrade(event.getFuelGrade().toString());
        entry.setPricePerLiter(event.getPricePerLiter());
        entry.setCurrency(event.getCurrency() != null ? event.getCurrency().name() : "EUR");
        entry.setChangedAt(event.getOccurredAt().atOffset(ZoneOffset.UTC));
        entry.setChangedBy(event.getPerformedBy().isSystem() ? null : event.getPerformedBy().id());
        fuelPriceHistoryRepository.save(entry);
    }

    @EventListener
    public void handle(FuelStationDeactivated event) {
        logger.info("Fuel station was deactivated ID:{}", event.getFuelStationId());
        createFuelStationEvent.process(event);
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/deactivated",
                event
        );
    }

    @EventListener
    public void handle(ManagerAssignedToFuelStation event) {
        logger.info("Manager was assigned to fuel station ID:{} MANAGER ID:{}", event.getFuelStationId(), event.getManagerId());
        createFuelStationEvent.process(event);
        this.fuelStationEmailService.sendManagerAssigned(event.getManagerId(), event.getFuelStationId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/manager-assigned",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/managers/" + event.getManagerId() + "/assigned-to-fuel-station",
                event
        );
    }

    @EventListener
    public void handle(ManagerUnassignedFromFuelStation event) {
        logger.info("Manager was unassigned from fuel station ID:{} MANAGER ID:{}", event.getFuelStationId(), event.getManagerId());
        createFuelStationEvent.process(event);
        this.fuelStationEmailService.sendManagerUnassigned(event.getManagerId(), event.getFuelStationId());
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/manager-unassigned",
                event
        );
        messagingTemplate.convertAndSend(
                "/topic/managers/" + event.getManagerId() + "/unassigned-from-fuel-station",
                event
        );
    }

    @EventListener
    public void handle(FuelTankInstalled event) {
        logger.info("Fuel tank was installed STATION ID:{} TANK ID:{} grade:{} capacity:{}",
                event.getFuelStationId(), event.getFuelTankId(), event.getFuelGrade(), event.getMaxCapacity());
        createFuelStationEvent.process(event);
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-tank-installed",
                event
        );
    }

    @EventListener
    public void handle(FuelTankDecommissioned event) {
        logger.info("Fuel tank was decommissioned STATION ID:{} TANK ID:{}", event.getFuelStationId(), event.getFuelTankId());
        createFuelStationEvent.process(event);
        messagingTemplate.convertAndSend(
                "/topic/fuel-stations/" + event.getFuelStationId() + "/fuel-tank-decommissioned",
                event
        );
    }

    @EventListener
    public void handle(FuelTankVolumeChanged event) {
        logger.debug("Fuel tank volume changed STATION ID:{} TANK ID:{} reason:{}",
                event.getFuelStationId(), event.getFuelTankId(), event.getReason());
        saveTankVolumeHistory(event);
    }

    private void saveTankVolumeHistory(FuelTankVolumeChanged event) {
        fuelTankVolumeHistoryRepository.save(new FuelTankVolumeHistory(
                null,
                event.getFuelStationId(),
                event.getFuelTankId(),
                event.getFuelGrade(),
                event.getOldVolume(),
                event.getNewVolume(),
                event.getReason(),
                event.getPerformedBy(),
                event.getOccurredAt()
        ));
    }

}
