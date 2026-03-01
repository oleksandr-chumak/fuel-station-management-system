package com.fuelstation.managmentapi.manager.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class ManagerEvent extends DomainEvent {

    private final ManagerEventType type;
    private final long managerId;

    protected ManagerEvent(ManagerEventType type, long managerId, Actor performedBy) {
        super(performedBy);
        this.type = type;
        this.managerId = managerId;
    }

}
