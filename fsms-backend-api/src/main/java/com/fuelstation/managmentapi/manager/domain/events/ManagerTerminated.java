package com.fuelstation.managmentapi.manager.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

public record ManagerTerminated(ManagerEventType type, long mangerId) implements DomainEvent {

    public ManagerTerminated(long managerId) {
        this(ManagerEventType.MANAGER_TERMINATED, managerId);
    }

}
