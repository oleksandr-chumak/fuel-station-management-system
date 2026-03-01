package com.fuelstation.managmentapi.manager.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;

public final class ManagerCreated extends ManagerEvent {

    public ManagerCreated(long managerId, Actor performedBy) {
        super(ManagerEventType.MANAGER_CREATED, managerId, performedBy);
    }

}
