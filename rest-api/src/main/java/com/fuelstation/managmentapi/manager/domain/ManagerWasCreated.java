package com.fuelstation.managmentapi.manager.domain;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManagerWasCreated implements DomainEvent {
    private long mangerId;
}
