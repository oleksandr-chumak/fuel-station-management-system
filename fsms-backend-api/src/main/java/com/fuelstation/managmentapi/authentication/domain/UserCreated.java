package com.fuelstation.managmentapi.authentication.domain;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEvent;

import lombok.Getter;

@Getter
public class UserCreated extends DomainEvent {
    private final long userId;
    private final String email;
    private final String plainPassword;
    private final UserRole role;

    public UserCreated(long userId, String email, String plainPassword, UserRole role, Actor performedBy) {
        super(performedBy);
        this.userId = userId;
        this.email = email;
        this.plainPassword = plainPassword;
        this.role = role;
    }
}
