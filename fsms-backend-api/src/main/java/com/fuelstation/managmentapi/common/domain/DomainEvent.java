package com.fuelstation.managmentapi.common.domain;

import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class DomainEvent {

    private final Actor performedBy;
    private final Instant occurredAt;

    protected DomainEvent(Actor performedBy) {
        this.occurredAt = Instant.now();
        this.performedBy = performedBy;
    }

    protected DomainEvent(Actor performedBy, Instant occurredAt) {
        this.occurredAt = occurredAt;
        this.performedBy = performedBy;
    }

}