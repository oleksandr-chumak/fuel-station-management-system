package com.fuelstation.managmentapi.authentication.application;

import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.domain.UserStatus;

public record UserResponse(
        Long userId,
        String firstName,
        String lastName,
        String fullName,
        String email,
        UserStatus status,
        UserRole role
) {

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getEmail(),
                user.getStatus(),
                user.getRole()
        );
    }
}
