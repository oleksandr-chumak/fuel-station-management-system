package com.fuelstation.managmentapi.manager.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

public record ManagerCreated(ManagerEventType type, long managerId) implements DomainEvent {

    public ManagerCreated(long managerId) {
        this(ManagerEventType.MANAGER_CREATED, managerId);
    }

}
