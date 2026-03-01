package com.fuelstation.managmentapi.manager.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;

public final class ManagerTerminated extends ManagerEvent {

    public ManagerTerminated(long managerId, Actor performedBy) {
        super(ManagerEventType.MANAGER_TERMINATED, managerId, performedBy);
    }
}
