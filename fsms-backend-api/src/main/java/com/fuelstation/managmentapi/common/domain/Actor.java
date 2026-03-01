package com.fuelstation.managmentapi.common.domain;

public record Actor(Long id, ActorType type) {

    public static Actor system() {
        return new Actor(null, ActorType.SYSTEM);
    }

    public static Actor user(long userId) {
        return new Actor(userId, ActorType.USER);
    }

    public boolean isSystem() {
        return type == ActorType.SYSTEM;
    }

    public boolean isUser() {
        return type == ActorType.USER;
    }

}