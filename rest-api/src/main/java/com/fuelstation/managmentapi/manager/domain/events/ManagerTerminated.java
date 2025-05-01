package com.fuelstation.managmentapi.manager.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManagerTerminated implements DomainEvent {
    private long mangerId;
}
