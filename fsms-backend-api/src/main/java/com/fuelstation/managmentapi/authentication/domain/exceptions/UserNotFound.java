package com.fuelstation.managmentapi.authentication.domain.exceptions;

public class UserNotFound extends UserDomainException {

    public UserNotFound(Long userId) {
        super("User #%d does not exist".formatted(userId), "NOT_FOUND");
    }

}
