package com.fuelstation.managmentapi.fuelorder.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.AggregateRoot;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderConfirmed;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderProcessed;
import com.fuelstation.managmentapi.fuelorder.domain.events.FuelOrderRejected;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeConfirmedException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeProcessedException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeRejectedException;

import lombok.*;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FuelOrder extends AggregateRoot {
    private Long fuelOrderId;
    private Long fuelStationId;
    private List<FuelOrderAllocation> allocations;
    private FuelOrderStatus status;
    private FuelGrade grade;
    private OffsetDateTime createdAt;
    private Long createdBy;

    public FuelOrder(
        Long fuelOrderId,
        Long fuelStationId,
        List<FuelOrderAllocation> allocations,
        FuelOrderStatus status,
        FuelGrade grade,
        OffsetDateTime createdAt,
        Long createdBy
    ) {
        this.fuelOrderId = fuelOrderId;
        this.fuelStationId = fuelStationId;
        this.allocations = allocations;
        this.status = status;
        this.grade = grade;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public BigDecimal getVolume() {
        return allocations.stream()
            .map(FuelOrderAllocation::volume)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void confirm(Actor performedBy, BigDecimal pricePerLiter, CurrencyCode currency) {
        Objects.requireNonNull(pricePerLiter, "pricePerLiter must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
        if (pricePerLiter.signum() <= 0) {
            throw new IllegalArgumentException("pricePerLiter must be positive");
        }
        if (status != FuelOrderStatus.PENDING) {
            throw new FuelOrderCannotBeConfirmedException(fuelOrderId, status);
        }
        status = FuelOrderStatus.CONFIRMED;
        pushDomainEvent(new FuelOrderConfirmed(fuelOrderId, fuelStationId, pricePerLiter, currency, performedBy));
    }

    public void reject(Actor performedBy) {
        if (status != FuelOrderStatus.PENDING) {
            throw new FuelOrderCannotBeRejectedException(fuelOrderId, status);
        }
        status = FuelOrderStatus.REJECTED;
        pushDomainEvent(new FuelOrderRejected(fuelOrderId, fuelStationId, performedBy));
    }

    public void process(Actor performedBy) {
        if (status != FuelOrderStatus.CONFIRMED) {
            throw new FuelOrderCannotBeProcessedException(fuelOrderId, status);
        }
        status = FuelOrderStatus.PROCESSED;
        pushDomainEvent(new FuelOrderProcessed(fuelOrderId, fuelStationId, performedBy));
    }

    public static FuelOrder create(
        Long fuelStationId,
        List<FuelOrderAllocation> allocations,
        FuelGrade fuelGrade,
        Actor actor
    ) {
        Objects.requireNonNull(fuelStationId, "fuelStationId must not be null");
        Objects.requireNonNull(fuelGrade, "fuelGrade must not be null");
        Objects.requireNonNull(actor, "actor must not be null");
        Objects.requireNonNull(allocations, "allocations must not be null");

        if (allocations.isEmpty()) {
            throw new IllegalArgumentException("Fuel order must contain at lest one allocation");
        }

        for (FuelOrderAllocation allocation : allocations) {
            Objects.requireNonNull(allocation, "allocation must not be null");
            Objects.requireNonNull(allocation.fuelTankId(), "fuelTankId must not be null");
            Objects.requireNonNull(allocation.volume(), "volume must not be null");
            if (allocation.volume().signum() <= 0) {
                throw new IllegalArgumentException(
                    "Allocation volume must be positive for tank " + allocation.fuelTankId()
                );
            }
        }

        long distinctTanks = allocations.stream()
            .map(FuelOrderAllocation::fuelTankId)
            .distinct()
            .count();
        if (distinctTanks != allocations.size()) {
            throw new IllegalArgumentException(
                "Fuel order cannot contain duplicate tank allocations"
            );
        }

        return new FuelOrder(
            null,
            fuelStationId,
            allocations,
            FuelOrderStatus.PENDING,
            fuelGrade,
            OffsetDateTime.now(),
            actor.id()
        );
    }
}
