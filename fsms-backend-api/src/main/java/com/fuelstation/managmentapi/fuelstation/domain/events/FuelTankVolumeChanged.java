package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEvent;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTankVolumeChangeReason;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class FuelTankVolumeChanged extends DomainEvent {
    private final long fuelStationId;
    private final long fuelTankId;
    private final FuelGrade fuelGrade;
    private final BigDecimal oldVolume;
    private final BigDecimal newVolume;
    private final FuelTankVolumeChangeReason reason;

    public FuelTankVolumeChanged(
            long fuelStationId,
            long fuelTankId,
            FuelGrade fuelGrade,
            BigDecimal oldVolume,
            BigDecimal newVolume,
            FuelTankVolumeChangeReason reason,
            Actor performedBy
    ) {
        super(performedBy);
        this.fuelStationId = fuelStationId;
        this.fuelTankId = fuelTankId;
        this.fuelGrade = fuelGrade;
        this.oldVolume = oldVolume;
        this.newVolume = newVolume;
        this.reason = reason;
    }

    public FuelTankVolumeChanged(
            long fuelStationId,
            long fuelTankId,
            FuelGrade fuelGrade,
            BigDecimal oldVolume,
            BigDecimal newVolume,
            FuelTankVolumeChangeReason reason,
            Actor performedBy,
            Instant occurredAt
    ) {
        super(performedBy, occurredAt);
        this.fuelStationId = fuelStationId;
        this.fuelTankId = fuelTankId;
        this.fuelGrade = fuelGrade;
        this.oldVolume = oldVolume;
        this.newVolume = newVolume;
        this.reason = reason;
    }
}
