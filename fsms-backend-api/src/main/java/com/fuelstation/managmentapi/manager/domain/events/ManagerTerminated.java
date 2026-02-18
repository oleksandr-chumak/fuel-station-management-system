package com.fuelstation.managmentapi.manager.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

public record ManagerTerminated(long mangerId) implements DomainEvent {
}
