package com.fuelstation.managmentapi.common.domain;

import com.fuelstation.managmentapi.authentication.domain.UserRole;

public record Actor(Long id, ActorType type, UserRole role) {

    public static Actor system() {
        return new Actor(null, ActorType.SYSTEM, null);
    }

    public static Actor user(long userId, UserRole role) {
        return new Actor(userId, ActorType.USER, role);
    }

    public boolean isSystem() {
        return type == ActorType.SYSTEM;
    }

    public boolean isUser() {
        return type == ActorType.USER;
    }

    public boolean isAdmin() { return isUser() && role == UserRole.ADMINISTRATOR; }

    public boolean isManager() { return isUser() && role == UserRole.MANAGER; }

}